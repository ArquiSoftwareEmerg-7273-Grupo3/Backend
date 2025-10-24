package com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories;

import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IlustracionRepository extends JpaRepository<Ilustracion, Long> {
    List<Ilustracion> findByPortafolios_Id(Long portafolioId);

    @Query("SELECT COALESCE(MAX(i.id), 0) FROM Ilustracion i WHERE i.ilustradorId = :ilustradorId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Long findMaxIdByIlustradorId(@Param("ilustradorId") Long ilustradorId);

    @Query("SELECT i FROM Ilustracion i WHERE i.publicada = true AND i.ilustradorId = :ilustradorId")
    List<Ilustracion> findIlustracionesPublicadasByIlustrador(@Param("ilustradorId") Long ilustradorId);

}
