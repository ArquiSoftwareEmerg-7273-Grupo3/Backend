package com.drawnet.artcollab.profiles.interfaces.rest.resources;

public record CreateEscritorResource(
        String firstName,
        String lastName,
        String biografia,
        String foto,
        String redes,
        Long suscripcion,
        Long userId
) {}
