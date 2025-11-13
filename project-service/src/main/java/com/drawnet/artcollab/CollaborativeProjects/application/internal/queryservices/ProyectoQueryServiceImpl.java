package com.drawnet.artcollab.CollaborativeProjects.application.internal.queryservices;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetAllProyectosQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetProyectosByEscritorIdQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.ProyectoQueryService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.ProyectoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoQueryServiceImpl implements ProyectoQueryService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoQueryServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public List<Proyecto> handle(GetAllProyectosQuery query) {
        return proyectoRepository.findAll();
    }

    @Override
    public List<Proyecto> getByEscritorId(Long escritorId) {
        return proyectoRepository.findByEscritorId(escritorId);
    }

    @Override
    public Optional<Proyecto> getById(Long id) {
        return proyectoRepository.findById(id);
    }
}