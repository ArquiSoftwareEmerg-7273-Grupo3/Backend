package com.drawnet.feed_service.infrastructure.external.clients;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Collections;

/**
 * Fallback para ChatServiceClient
 */
@Component
public class ChatServiceClientFallback implements ChatServiceClient {

    @Override
    public void sendFeedInteractionNotification(FeedInteractionNotificationDto notification) {
        // Log del fallo pero no hacer nada más (las notificaciones no son críticas)
        System.out.println("Chat service unavailable - notification not sent: " + notification.type());
    }

    @Override
    public List<String> getOnlineUsers() {
        return Collections.emptyList();
    }

    @Override
    public void sendPushNotification(PushNotificationDto notification) {
        // Log del fallo
        System.out.println("Chat service unavailable - push notification not sent to: " + notification.userId());
    }

    @Override
    public boolean isUserOnline(String userId) {
        // Asumir que el usuario está offline en caso de fallo
        return false;
    }
}