package com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8088", path = "/api/v1/notifications")
public interface NotificationClient {
    
    @PostMapping("/generic")
    void createNotification(@RequestBody CreateNotificationRequest request);
}
