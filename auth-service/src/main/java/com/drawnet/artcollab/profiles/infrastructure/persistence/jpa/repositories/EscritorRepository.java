package com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories;


import com.drawnet.artcollab.profiles.domain.model.aggregates.Escritor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EscritorRepository extends JpaRepository<Escritor, Long> {


}
