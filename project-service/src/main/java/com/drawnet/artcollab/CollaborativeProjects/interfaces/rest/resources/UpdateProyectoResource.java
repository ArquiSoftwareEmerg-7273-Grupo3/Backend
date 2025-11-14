package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateProyectoResource(
    String titulo,
    String descripcion,
    BigDecimal presupuesto,
    ModalidadProyecto modalidadProyecto,
    ContratoProyecto contratoProyecto,
    EspecialidadProyecto especialidadProyecto,
    String requisitos,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    Integer maxPostulaciones
) {}
