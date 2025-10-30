package com.drawnet.artcollab.profiles.domain.model.commands;

public record CreateEscritorCommand(
    String razonSocial,      // no obligatorio
    String ruc,              // no obligatorio  
    String nombreComercial,  // no obligatorio
    String sitioWeb,         // no obligatorio
    String logo,             // no obligatorio
    String ubicacionEmpresa, // no obligatorio
    String tipoEmpresa,      // no obligatorio
    Long userId              // obligatorio
) {
    public CreateEscritorCommand {
        if (userId == null) {
            throw new IllegalArgumentException("El ID de usuario es obligatorio");
        }
    }
}
