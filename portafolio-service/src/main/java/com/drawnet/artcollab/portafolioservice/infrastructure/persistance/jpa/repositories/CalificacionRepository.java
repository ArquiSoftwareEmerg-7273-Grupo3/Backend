package com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories;

import com.drawnet.artcollab.portafolioservice.domain.model.valueobjects.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByIlustracionId(Long ilustracionId);
}
