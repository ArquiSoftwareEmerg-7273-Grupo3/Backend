package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

public record IlustracionResumenResource(
        String titulo,
        String descripcion,
        double promedioCalificaciones,
        int cantidadCalificaciones
) { }
