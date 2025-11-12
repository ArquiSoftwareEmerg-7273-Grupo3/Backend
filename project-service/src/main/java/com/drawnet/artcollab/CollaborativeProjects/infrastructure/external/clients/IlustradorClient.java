package com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", path = "/api/v1/ilustradores")
public interface IlustradorClient {
    @GetMapping("/by-user/{userId}")
    IlustradorResource getIlustradorByUserId(@PathVariable("userId") Long userId);
}
