package com.drawnet.artcollab.CollaborativeProjects.application.internal.commandservices;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreateProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.ProyectoCommandService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.ProyectoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProyectoCommandServiceImpl implements ProyectoCommandService {
    private final ProyectoRepository proyectoRepository;

    public ProyectoCommandServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Optional<Proyecto> handle(CreateProyectoCommand command) {
        //if (proyectoRepository.existsByTituloAndEscritorId(command.titulo(), command.escritorId())) {
        //    throw new IllegalArgumentException("Ya existe un proyecto con el título '" + command.titulo());
        //}
        //if (!proyectoRepository.existsByEscritorId(command.escritorId())) {
        //    throw new IllegalArgumentException("El escritor con id " + command.escritorId() + " no existe");
        //}
        var proyecto = new Proyecto(command);
        var createdProyecto = proyectoRepository.save(proyecto);
        return Optional.of(createdProyecto);
    }

}
