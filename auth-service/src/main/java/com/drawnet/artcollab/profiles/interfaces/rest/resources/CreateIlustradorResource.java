package com.drawnet.artcollab.profiles.interfaces.rest.resources;

public record CreateIlustradorResource(
        String firstName,
        String lastName,
        String biografia,
        String foto,
        String redes,
        Long suscripcion,
        Long userId
) {
}
