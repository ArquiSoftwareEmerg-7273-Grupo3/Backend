package com.drawnet.artcollab.chatservice.infrastructure.persistance.jpa.repositories;

import com.drawnet.artcollab.chatservice.domain.model.aggregates.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
