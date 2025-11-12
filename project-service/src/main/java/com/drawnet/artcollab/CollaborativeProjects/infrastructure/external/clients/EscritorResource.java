package com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients;

public record EscritorResource(
    Long id,
    Long userId,
    String nombreArtistico,
    String biografia
) {}
