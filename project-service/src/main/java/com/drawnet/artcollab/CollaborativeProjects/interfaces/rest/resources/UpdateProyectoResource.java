package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateProyectoResource(
    String titulo,
    String descripcion,
    BigDecimal presupuesto,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    Integer maxPostulaciones
) {}
