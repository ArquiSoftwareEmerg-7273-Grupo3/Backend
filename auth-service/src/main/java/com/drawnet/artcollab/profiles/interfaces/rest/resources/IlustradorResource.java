package com.drawnet.artcollab.profiles.interfaces.rest.resources;

public record IlustradorResource(
    Long id, 
    String nombreArtistico, 
    Boolean subscripcion,
    Long userId,
    String username,
    String nombres,
    String apellidos
) {
}
