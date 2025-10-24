package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

public record PublicarIlustracionResource(
        String titulo,
        String descripcion,
        String urlImagen,
        boolean publicada
) {
}
