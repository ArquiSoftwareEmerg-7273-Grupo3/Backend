package com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories;

import com.drawnet.artcollab.portafolioservice.domain.model.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    /**
     * Buscar todas las categorías de un portafolio, ordenadas por orden
     */
    List<Categoria> findByPortafolioIdOrderByOrdenAsc(Long portafolioId);
    
    /**
     * Buscar categoría por ID con sus ilustraciones cargadas
     */
    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.ilustraciones WHERE c.id = :id")
    Optional<Categoria> findByIdWithIlustraciones(@Param("id") Long id);
    
    /**
     * Contar categorías de un portafolio
     */
    long countByPortafolioId(Long portafolioId);
    
    /**
     * Verificar si existe una categoría con ese nombre en un portafolio
     */
    boolean existsByPortafolioIdAndNombre(Long portafolioId, String nombre);
    
    /**
     * Buscar categorías por portafolio con ilustraciones
     */
    @Query("SELECT DISTINCT c FROM Categoria c LEFT JOIN FETCH c.ilustraciones WHERE c.portafolio.id = :portafolioId ORDER BY c.orden ASC")
    List<Categoria> findByPortafolioIdWithIlustraciones(@Param("portafolioId") Long portafolioId);
}
