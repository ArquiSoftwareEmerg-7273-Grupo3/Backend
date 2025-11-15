package com.drawnet.artcollab.notificacionservice.application.internal.queryservices;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetAllNotificationsByUserQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetNotificationByIdQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetUnreadNotificationsByUserQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationStatus;
import com.drawnet.artcollab.notificacionservice.domain.services.NotificationQueryService;
import com.drawnet.artcollab.notificacionservice.infrastructure.persistence.jpa.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> handle(GetAllNotificationsByUserQuery query) {
        // Todas las notificaciones del usuario, ordenadas de la más reciente a la más antigua
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(query.userId());
    }

    @Override
    public List<Notification> handle(GetUnreadNotificationsByUserQuery query) {
        // Solo las notificaciones en estado PENDIENTE
        return notificationRepository.findByRecipientIdAndStatusOrderByCreatedAtDesc(
                query.userId(),
                NotificationStatus.PENDIENTE
        );
    }

    @Override
    public Optional<Notification> handle(GetNotificationByIdQuery query) {
        return notificationRepository.findById(query.notificationId());
    }

}
