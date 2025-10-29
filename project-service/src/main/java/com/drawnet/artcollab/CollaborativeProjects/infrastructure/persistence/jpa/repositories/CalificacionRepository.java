package com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByIlustracionId(Long ilustracionId);
}
