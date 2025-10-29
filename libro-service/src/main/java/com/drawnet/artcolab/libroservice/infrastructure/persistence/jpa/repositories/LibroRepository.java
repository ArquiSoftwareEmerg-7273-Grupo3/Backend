package com.drawnet.artcolab.libroservice.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcolab.libroservice.domain.model.aggregates.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByEscritorId(Long escritorId);
}
