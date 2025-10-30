package com.drawnet.artcollab.profiles.interfaces.rest.resources;

public record EscritorResource(
    Long id,
    String razonSocial,
    String ruc,
    String nombreComercial,
    String sitioWeb,
    String logo,
    String ubicacionEmpresa,
    String tipoEmpresa,
    Long userId,
    String username,
    String nombres,
    String apellidos
) {}
