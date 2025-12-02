package com.drawnet.artcollab.notificacionservice.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Obtiene todas las notificaciones de un usuario (paginadas)
     */
    Page<Notification> findByRecipientUserIdAndActiveTrue(Long recipientUserId, Pageable pageable);
    
    /**
     * Obtiene las notificaciones no leídas de un usuario (paginadas)
     */
    Page<Notification> findByRecipientUserIdAndIsReadFalseAndActiveTrue(Long recipientUserId, Pageable pageable);
    
    /**
     * Obtiene las notificaciones por tipo (paginadas)
     */
    Page<Notification> findByRecipientUserIdAndTypeAndActiveTrue(Long recipientUserId, NotificationType type, Pageable pageable);
    
    /**
     * Cuenta las notificaciones no leídas de un usuario
     */
    Long countByRecipientUserIdAndIsReadFalseAndActiveTrue(Long recipientUserId);
    
    /**
     * Obtiene una notificación por ID y usuario (para validar permisos)
     */
    Optional<Notification> findByIdAndRecipientUserIdAndActiveTrue(Long id, Long recipientUserId);
    
    /**
     * Marca todas las notificaciones de un usuario como leídas
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.recipientUserId = :userId AND n.isRead = false AND n.active = true")
    int markAllAsReadByUserId(@Param("userId") Long userId, @Param("readAt") LocalDateTime readAt);
    
    /**
     * Obtiene las notificaciones más recientes de un usuario (últimas N)
     */
    List<Notification> findTop10ByRecipientUserIdAndActiveTrueOrderByCreatedAtDesc(Long recipientUserId);
    
    /**
     * Elimina (soft delete) notificaciones expiradas
     */
    @Modifying
    @Query("UPDATE Notification n SET n.active = false WHERE n.expiresAt IS NOT NULL AND n.expiresAt < :now AND n.active = true")
    int deactivateExpiredNotifications(@Param("now") LocalDateTime now);
    
    /**
     * Obtiene notificaciones por entidad relacionada
     */
    List<Notification> findByRelatedEntityTypeAndRelatedEntityIdAndActiveTrue(String entityType, Long entityId);
}
