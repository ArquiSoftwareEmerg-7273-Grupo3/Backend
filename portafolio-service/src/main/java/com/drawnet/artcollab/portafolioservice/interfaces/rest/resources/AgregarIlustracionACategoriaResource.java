package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

public record AgregarIlustracionACategoriaResource(
        Long ilustracionId, // null para nueva, Long para existente
        String titulo,
        String descripcion,
        String urlImagen,
        Boolean publicada
) {
}
