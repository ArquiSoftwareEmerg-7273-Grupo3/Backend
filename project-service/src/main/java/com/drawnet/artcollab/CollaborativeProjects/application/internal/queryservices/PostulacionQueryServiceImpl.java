package com.drawnet.artcollab.CollaborativeProjects.application.internal.queryservices;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetAllPostulacionesQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.PostulacionQueryService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.PostulacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostulacionQueryServiceImpl implements PostulacionQueryService {

    private final PostulacionRepository postulacionRepository;

    public PostulacionQueryServiceImpl(PostulacionRepository postulacionRepository) {
        this.postulacionRepository = postulacionRepository;
    }

    @Override
    public List<Postulacion> handle(GetAllPostulacionesQuery query) {
        return postulacionRepository.findAll();
    }


    @Override
    public List<Postulacion> getByIlustradorId(Long ilustradorId) {
        return postulacionRepository.findByIlustradorId(ilustradorId);
    }

    @Override
    public List<Postulacion> getByProyectoId(Long proyectoId) {
        return postulacionRepository.findByProyectoId(proyectoId);
    }

}