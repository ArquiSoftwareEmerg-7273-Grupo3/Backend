package com.drawnet.artcollab.iam.interfaces.rest.resources;

import java.time.LocalDate;
import java.util.Map;

public record UserResource(
    Long id,
    String username,
    String ubicacion,
    String nombres,
    String apellidos,
    String telefono,
    String foto,
    String descripcion,
    LocalDate fechaNacimiento,
    Map<String, String> redesSociales,
    String roleName,
    // Información de perfiles especializados
    IlustradorProfileResource ilustrador,
    EscritorProfileResource escritor
) {
    // Records anidados para los perfiles
    public record IlustradorProfileResource(
        Long id,
        String nombreArtistico,
        Boolean subscripcion
    ) {}
    
    public record EscritorProfileResource(
        Long id,
        String razonSocial,
        String ruc,
        String nombreComercial,
        String sitioWeb,
        String logo,
        String ubicacionEmpresa,
        String tipoEmpresa
    ) {}
}

//lo q recibo