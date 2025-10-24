package com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Suscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {

    Optional<Suscripcion> findByUsuarioId(Long usuarioId);
}
