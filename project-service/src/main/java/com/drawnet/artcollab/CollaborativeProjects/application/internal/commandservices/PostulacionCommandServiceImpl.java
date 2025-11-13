package com.drawnet.artcollab.CollaborativeProjects.application.internal.commandservices;

import com.drawnet.artcollab.CollaborativeProjects.domain.exceptions.BusinessException;
import com.drawnet.artcollab.CollaborativeProjects.domain.exceptions.DuplicatePostulacionException;
import com.drawnet.artcollab.CollaborativeProjects.domain.exceptions.ResourceNotFoundException;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.AprobarPostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CancelarPostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreatePostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.RechazarPostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.events.PostulacionAprobadaEvent;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.events.PostulacionCreadaEvent;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.events.PostulacionRechazadaEvent;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.PostulacionCommandService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.PostulacionRepository;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.ProyectoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostulacionCommandServiceImpl implements PostulacionCommandService {

    private final PostulacionRepository postulacionRepository;
    private final ProyectoRepository proyectoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PostulacionCommandServiceImpl(PostulacionRepository postulacionRepository, 
                                        ProyectoRepository proyectoRepository,
                                        ApplicationEventPublisher eventPublisher) {
        this.postulacionRepository = postulacionRepository;
        this.proyectoRepository = proyectoRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Optional<Postulacion> handle(CreatePostulacionCommand command) {
        Proyecto proyecto = proyectoRepository.findById(command.proyectoId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "El proyecto con id " + command.proyectoId() + " no existe"));
        
        if (!proyecto.estaAbierto()) {
            throw new BusinessException("El proyecto no está abierto para postulaciones");
        }
        
        if (postulacionRepository.hasActivePostulacion(command.proyectoId(), command.ilustradorId())) {
            throw new DuplicatePostulacionException(
                "Ya tienes una postulación activa para este proyecto");
        }
        
        long totalPostulaciones = postulacionRepository.countByProyectoId(command.proyectoId());
        if (proyecto.getMaxPostulaciones() != null && totalPostulaciones >= proyecto.getMaxPostulaciones()) {
            throw new BusinessException("El proyecto ha alcanzado el límite de postulaciones");
        }
        
        var postulacion = new Postulacion(command);
        var createdPostulacion = postulacionRepository.save(postulacion);
        
        eventPublisher.publishEvent(new PostulacionCreadaEvent(
            this,
            createdPostulacion.getId(),
            proyecto.getId(),
            command.ilustradorId(),
            proyecto.getEscritorId(),
            command.mensaje()
        ));
        
        return Optional.of(createdPostulacion);
    }

    @Override
    @Transactional
    public Optional<Postulacion> handle(AprobarPostulacionCommand command) {
        Postulacion postulacion = postulacionRepository.findById(command.postulacionId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "La postulación con id " + command.postulacionId() + " no existe"));
        
        Proyecto proyecto = proyectoRepository.findById(postulacion.getProyectoId())
            .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        
        if (!proyecto.perteneceA(command.escritorId())) {
            throw new BusinessException("Solo el escritor del proyecto puede aprobar postulaciones");
        }
        
        postulacion.aprobar(command.respuesta(), command.escritorId());
        proyecto.iniciar(postulacion.getIlustradorId());
        
        postulacionRepository.save(postulacion);
        proyectoRepository.save(proyecto);
        
        eventPublisher.publishEvent(new PostulacionAprobadaEvent(
            this,
            postulacion.getId(),
            proyecto.getId(),
            postulacion.getIlustradorId(),
            command.respuesta()
        ));
        
        return Optional.of(postulacion);
    }

    @Override
    @Transactional
    public Optional<Postulacion> handle(RechazarPostulacionCommand command) {
        Postulacion postulacion = postulacionRepository.findById(command.postulacionId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "La postulación con id " + command.postulacionId() + " no existe"));
        
        Proyecto proyecto = proyectoRepository.findById(postulacion.getProyectoId())
            .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        
        if (!proyecto.perteneceA(command.escritorId())) {
            throw new BusinessException("Solo el escritor del proyecto puede rechazar postulaciones");
        }
        
        postulacion.rechazar(command.razon(), command.escritorId());
        postulacionRepository.save(postulacion);
        
        eventPublisher.publishEvent(new PostulacionRechazadaEvent(
            this,
            postulacion.getId(),
            proyecto.getId(),
            postulacion.getIlustradorId(),
            command.razon()
        ));
        
        return Optional.of(postulacion);
    }

    @Override
    @Transactional
    public Optional<Postulacion> handle(CancelarPostulacionCommand command) {
        Postulacion postulacion = postulacionRepository.findById(command.postulacionId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "La postulación con id " + command.postulacionId() + " no existe"));
        
        postulacion.cancelar(command.ilustradorId());
        postulacionRepository.save(postulacion);
        
        return Optional.of(postulacion);
    }
}