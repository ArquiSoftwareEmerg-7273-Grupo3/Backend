package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateProyectoCommand(
    Long proyectoId,
    Long escritorId,
    String titulo,
    String descripcion,
    BigDecimal presupuesto,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    Integer maxPostulaciones
) {}
