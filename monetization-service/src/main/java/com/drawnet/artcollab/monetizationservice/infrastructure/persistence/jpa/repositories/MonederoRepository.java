package com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Monedero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonederoRepository extends JpaRepository<Monedero, Long> {

    Optional<Monedero> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioId(Long usuarioId);
}