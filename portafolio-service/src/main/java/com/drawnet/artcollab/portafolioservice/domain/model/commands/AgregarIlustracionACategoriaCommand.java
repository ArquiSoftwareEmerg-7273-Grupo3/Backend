package com.drawnet.artcollab.portafolioservice.domain.model.commands;

public record AgregarIlustracionACategoriaCommand(
        Long categoriaId,
        Long ilustradorId,
        Long ilustracionId, // null si es nueva, Long si es existente
        String titulo,
        String descripcion,
        String urlImagen,
        Boolean publicada
) {
}
