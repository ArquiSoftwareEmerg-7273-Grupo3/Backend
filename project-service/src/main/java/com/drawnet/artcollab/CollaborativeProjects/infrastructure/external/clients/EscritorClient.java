package com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service-escritor", url = "${auth-service.url:http://localhost:8082}", path = "/api/v1")
public interface EscritorClient {
    @GetMapping("/escritores/by-user/{userId}")
    EscritorResource obtenerEscritorPorUserId(@PathVariable Long userId);}
