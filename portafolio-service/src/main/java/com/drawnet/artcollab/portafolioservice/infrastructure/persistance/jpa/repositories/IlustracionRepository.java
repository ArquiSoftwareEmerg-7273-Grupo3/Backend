package com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories;

import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IlustracionRepository extends JpaRepository<Ilustracion, Long> {
    
    /**
     * Busca ilustraciones por categoría.
     * Reemplaza el antiguo findByPortafolios_Id que usaba la relación ManyToMany.
     */
    List<Ilustracion> findByCategoria_Id(Long categoriaId);
    
    /**
     * Busca ilustraciones por portafolio a través de la categoría.
     */
    @Query("SELECT i FROM Ilustracion i JOIN i.categoria c WHERE c.portafolio.id = :portafolioId")
    List<Ilustracion> findByPortafolioId(@Param("portafolioId") Long portafolioId);

    @Query("SELECT COALESCE(MAX(i.id), 0) FROM Ilustracion i WHERE i.ilustradorId = :ilustradorId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Long findMaxIdByIlustradorId(@Param("ilustradorId") Long ilustradorId);

    @Query("SELECT i FROM Ilustracion i WHERE i.publicada = true AND i.ilustradorId = :ilustradorId")
    List<Ilustracion> findIlustracionesPublicadasByIlustrador(@Param("ilustradorId") Long ilustradorId);

}
