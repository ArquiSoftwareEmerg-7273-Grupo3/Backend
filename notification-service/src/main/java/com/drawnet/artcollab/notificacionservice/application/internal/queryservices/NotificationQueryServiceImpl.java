package com.drawnet.artcollab.notificacionservice.application.internal.queryservices;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetAllNotificationsByUserQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetNotificationByIdQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetNotificationsByTypeQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetNotificationsByUserIdQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetUnreadNotificationsByUserQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetUnreadNotificationsCountQuery;
import com.drawnet.artcollab.notificacionservice.domain.services.NotificationQueryService;
import com.drawnet.artcollab.notificacionservice.infrastructure.persistence.jpa.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    @Override
    // @Cacheable(value = "notifications", key = "#query.userId()") // Deshabilitado temporalmente
    public List<Notification> handle(GetAllNotificationsByUserQuery query) {
        // Obtener las últimas 50 notificaciones del usuario
        Pageable pageable = PageRequest.of(0, 50);
        return notificationRepository.findByRecipientUserIdAndActiveTrue(query.userId(), pageable).getContent();
    }

    @Override
    // @Cacheable(value = "unreadNotifications", key = "#query.userId()") // Deshabilitado temporalmente
    public List<Notification> handle(GetUnreadNotificationsByUserQuery query) {
        // Obtener las últimas 50 notificaciones no leídas
        Pageable pageable = PageRequest.of(0, 50);
        return notificationRepository.findByRecipientUserIdAndIsReadFalseAndActiveTrue(query.userId(), pageable).getContent();
    }

    @Override
    public Optional<Notification> handle(GetNotificationByIdQuery query) {
        return notificationRepository.findById(query.notificationId());
    }

    @Override
    public Page<Notification> handle(GetNotificationsByUserIdQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        return notificationRepository.findByRecipientUserIdAndActiveTrue(query.userId(), pageable);
    }

    @Override
    public Long handle(GetUnreadNotificationsCountQuery query) {
        return notificationRepository.countByRecipientUserIdAndIsReadFalseAndActiveTrue(query.userId());
    }

    @Override
    public Page<Notification> handle(GetNotificationsByTypeQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        return notificationRepository.findByRecipientUserIdAndTypeAndActiveTrue(
                query.userId(), 
                query.type(), 
                pageable
        );
    }

    @Override
    public List<Notification> getRecentNotifications(Long userId) {
        return notificationRepository.findTop10ByRecipientUserIdAndActiveTrueOrderByCreatedAtDesc(userId);
    }
}
