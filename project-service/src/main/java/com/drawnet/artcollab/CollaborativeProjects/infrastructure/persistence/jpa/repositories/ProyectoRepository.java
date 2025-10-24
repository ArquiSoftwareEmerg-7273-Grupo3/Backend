package com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    boolean existsByEscritorId(Long id);
    boolean existsByTituloAndEscritorId(String titulo, Long escritorId);
    List<Proyecto> findByEscritorId(Long escritorId);
}
