package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects.*;

public record UpdateProyectoCommand(
    Long proyectoId,
    Long escritorId,
    String titulo,
    String descripcion,
    BigDecimal presupuesto,
    ModalidadProyecto modalidadProyecto,
    ContratoProyecto contratoProyecto,
    EspecialidadProyecto especialidadProyecto,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    Integer maxPostulaciones
) {}
