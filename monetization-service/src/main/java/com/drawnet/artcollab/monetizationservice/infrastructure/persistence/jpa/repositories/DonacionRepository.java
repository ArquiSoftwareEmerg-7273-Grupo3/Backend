package com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {

    Optional<Donacion> findById(Long id);
    List<Donacion> findByDonanteId(Long donanteId);
    List<Donacion> findByReceptorId(Long donacionId);
}