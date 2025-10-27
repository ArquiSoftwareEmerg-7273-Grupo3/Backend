package com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories;


import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByChatId(Long chatId);
   List<Mensaje> findByRemitenteId(Long remitenteId);
}
