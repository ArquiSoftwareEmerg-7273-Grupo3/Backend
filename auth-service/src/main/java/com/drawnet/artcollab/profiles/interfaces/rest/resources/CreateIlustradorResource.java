package com.drawnet.artcollab.profiles.interfaces.rest.resources;

public record CreateIlustradorResource(
        String nombreArtistico
        // userId se obtendrá del token JWT
) {
}
