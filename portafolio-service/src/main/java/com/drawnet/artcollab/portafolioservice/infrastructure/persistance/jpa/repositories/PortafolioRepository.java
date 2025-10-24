package com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories;

import com.drawnet.artcollab.portafolioservice.domain.model.aggregates.Portafolio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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
