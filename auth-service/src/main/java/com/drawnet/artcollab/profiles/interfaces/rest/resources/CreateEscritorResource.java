package com.drawnet.artcollab.profiles.interfaces.rest.resources;

public record CreateEscritorResource(
        String razonSocial,      // no obligatorio
        String ruc,              // no obligatorio  
        String nombreComercial,  // no obligatorio
        String sitioWeb,         // no obligatorio
        String logo,             // no obligatorio
        String ubicacionEmpresa, // no obligatorio
        String tipoEmpresa       // no obligatorio
        // userId se obtendrá del token JWT
) {}
