package com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Portafolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortafolioRepository extends JpaRepository<Portafolio, Long> {
    List<Portafolio> findByIlustradorId(Long ilustradorId);

    @Query("SELECT COALESCE(MAX(p.id), 0) FROM Portafolio p WHERE p.ilustradorId = :ilustradorId")
    Long findMaxIdByIlustradorId(Long ilustradorId);

    //@Query("SELECT COALESCE(MAX(p.id), 0) FROM Portafolio p WHERE p.ilustradorId = :ilustradorId")
    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    //Long findMaxIdByIlustradorId(Long ilustradorId);
}
