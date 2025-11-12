package com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients;

public record IlustradorResource(
    Long id,
    Long userId,
    String nombreArtistico,
    String biografia
) {}
