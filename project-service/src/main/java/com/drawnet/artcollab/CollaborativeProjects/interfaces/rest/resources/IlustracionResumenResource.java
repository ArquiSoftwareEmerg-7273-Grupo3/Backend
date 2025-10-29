package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

public record IlustracionResumenResource(
        String titulo,
        String descripcion,
        double promedioCalificaciones,
        int cantidadCalificaciones
) { }
