package com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    boolean existsByProyectoIdAndIlustradorId(Long proyectoId, Long ilustradorId);
    boolean existsByProyectoId(Long proyectoId);
    boolean existsByIlustradorId(Long ilustradorId);
    List<Postulacion> findByProyectoId(Long proyectoId);
    List<Postulacion> findByIlustradorId(Long ilustradorId);
}
