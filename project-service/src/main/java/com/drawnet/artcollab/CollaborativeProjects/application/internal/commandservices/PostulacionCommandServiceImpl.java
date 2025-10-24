package com.drawnet.artcollab.CollaborativeProjects.application.internal.commandservices;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreatePostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.PostulacionCommandService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.PostulacionRepository;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.ProyectoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostulacionCommandServiceImpl implements PostulacionCommandService {

    private final PostulacionRepository postulacionCommandService;
    private final ProyectoRepository proyectoRepository;

    public PostulacionCommandServiceImpl(PostulacionRepository postulacionCommandService, ProyectoRepository proyectoRepository) {
        this.postulacionCommandService = postulacionCommandService;
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Optional<Postulacion> handle(CreatePostulacionCommand command) {
        // Validar que el proyecto exista
        if (!proyectoRepository.existsById(command.proyectoId())) {
            throw new IllegalArgumentException("El proyecto con id " + command.proyectoId() + " no existe");
        }
        var postulacion = new Postulacion(command);
        var createdPostulacion = postulacionCommandService.save(postulacion);
        return Optional.of(createdPostulacion);
    }

    // src/main/java/com/drawnet/artcollab/CollaborativeProjects/application/internal/commandservices/PostulacionCommandServiceImpl.java
    @Override
    public Optional<Postulacion> actualizarEstado(Long id, String nuevoEstado) {
        var postulacion = postulacionCommandService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La postulación con id " + id + " no existe"));

        if (!"CONFIRMADO".equalsIgnoreCase(nuevoEstado) && !"RECHAZADO".equalsIgnoreCase(nuevoEstado)) {
            throw new IllegalArgumentException("Estado inválido. Solo se permite CONFIRMADO o RECHAZADO.");
        }

        postulacion.setEstado(nuevoEstado.toUpperCase());
        var updatedPostulacion = postulacionCommandService.save(postulacion);
        return Optional.of(updatedPostulacion);
    }
}