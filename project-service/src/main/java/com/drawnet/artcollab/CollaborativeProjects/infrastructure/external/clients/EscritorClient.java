package com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", path = "/api/v1/escritores")
public interface EscritorClient {
    @GetMapping("/by-user/{userId}")
    EscritorResource getEscritorByUserId(@PathVariable("userId") Long userId);
}
