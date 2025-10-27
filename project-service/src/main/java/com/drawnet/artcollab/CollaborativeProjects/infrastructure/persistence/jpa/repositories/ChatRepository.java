package com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories;


import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
