package com.drawnet.artcollab.CollaborativeProjects.application.internal.commandservices;

import com.drawnet.artcollab.CollaborativeProjects.domain.exceptions.BusinessException;
import com.drawnet.artcollab.CollaborativeProjects.domain.exceptions.ResourceNotFoundException;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CerrarProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreateProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.FinalizarProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.UpdateProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.ProyectoCommandService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.ProyectoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProyectoCommandServiceImpl implements ProyectoCommandService {
    private final ProyectoRepository proyectoRepository;

    public ProyectoCommandServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    @Transactional
    public Optional<Proyecto> handle(CreateProyectoCommand command) {
        // Validaciones
        if (command.fechaInicio() != null && command.fechaFin() != null) {
            if (command.fechaInicio().isAfter(command.fechaFin())) {
                throw new BusinessException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }
        }
        
        if (command.presupuesto() != null && command.presupuesto().signum() < 0) {
            throw new BusinessException("El presupuesto no puede ser negativo");
        }
        
        var proyecto = new Proyecto(command);
        var createdProyecto = proyectoRepository.save(proyecto);
        return Optional.of(createdProyecto);
    }

    @Override
    @Transactional
    public Optional<Proyecto> handle(UpdateProyectoCommand command) {
        Proyecto proyecto = proyectoRepository.findById(command.proyectoId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "El proyecto con id " + command.proyectoId() + " no existe"));
        
        if (!proyecto.perteneceA(command.escritorId())) {
            throw new BusinessException("Solo el escritor del proyecto puede actualizarlo");
        }
        
        if (!proyecto.estaAbierto()) {
            throw new BusinessException("Solo se pueden actualizar proyectos abiertos");
        }
        
        // Validar fechas
        if (command.fechaInicio() != null && command.fechaFin() != null) {
            if (command.fechaInicio().isAfter(command.fechaFin())) {
                throw new BusinessException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }
        }
        
        // Actualizar campos (usando reflection o setters - aquí necesitarías agregar setters al Proyecto)
        // Por ahora, esto requeriría modificar la entidad Proyecto para tener métodos update
        
        proyectoRepository.save(proyecto);
        return Optional.of(proyecto);
    }

    @Override
    @Transactional
    public Optional<Proyecto> handle(CerrarProyectoCommand command) {
        Proyecto proyecto = proyectoRepository.findById(command.proyectoId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "El proyecto con id " + command.proyectoId() + " no existe"));
        
        if (!proyecto.perteneceA(command.escritorId())) {
            throw new BusinessException("Solo el escritor del proyecto puede cerrarlo");
        }
        
        proyecto.cerrar();
        proyectoRepository.save(proyecto);
        return Optional.of(proyecto);
    }

    @Override
    @Transactional
    public Optional<Proyecto> handle(FinalizarProyectoCommand command) {
        Proyecto proyecto = proyectoRepository.findById(command.proyectoId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "El proyecto con id " + command.proyectoId() + " no existe"));
        
        if (!proyecto.perteneceA(command.escritorId())) {
            throw new BusinessException("Solo el escritor del proyecto puede finalizarlo");
        }
        
        proyecto.finalizar();
        proyectoRepository.save(proyecto);
        return Optional.of(proyecto);
    }
}
