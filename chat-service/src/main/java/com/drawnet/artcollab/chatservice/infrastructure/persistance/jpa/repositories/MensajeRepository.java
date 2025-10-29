package com.drawnet.artcollab.chatservice.infrastructure.persistance.jpa.repositories;

import com.drawnet.artcollab.chatservice.domain.model.aggregates.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByChatId(Long chatId);
   List<Mensaje> findByRemitenteId(Long remitenteId);
}
