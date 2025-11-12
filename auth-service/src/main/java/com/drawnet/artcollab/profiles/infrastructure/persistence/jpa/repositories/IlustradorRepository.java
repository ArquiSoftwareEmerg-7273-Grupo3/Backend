package com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories;


import com.drawnet.artcollab.profiles.domain.model.aggregates.Ilustrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IlustradorRepository extends JpaRepository<Ilustrador, Long> {
    
    /**
     * Busca un ilustrador por el userId (ID del User asociado)
     */
    @Query("SELECT i FROM Ilustrador i WHERE i.user.id = :userId")
    Optional<Ilustrador> findByUserId(@Param("userId") Long userId);

}
