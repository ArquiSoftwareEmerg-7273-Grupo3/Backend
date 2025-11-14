package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateProyectoCommand(
    Long escritorId,
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
) {
    public CreateProyectoCommand {
        if (escritorId == null || escritorId < 0) {
            throw new IllegalArgumentException("EscritorId no puede ser nulo o menor que 0");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalArgumentException("Titulo no puede ser nulo o vacio");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new IllegalArgumentException("Descripcion no puede ser nulo o vacio");
        }
        if (presupuesto == null || presupuesto.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Presupuesto no puede ser nulo o negativo");
        }
        if (modalidadProyecto == null) {
            throw new IllegalArgumentException("ModalidadProyecto no puede ser nulo");
        }
        if (contratoProyecto == null) {
            throw new IllegalArgumentException("ContratoProyecto no puede ser nulo");
        }
        if(especialidadProyecto == null){
            throw new IllegalArgumentException("EspecialidadProyecto no puede ser nulo");
        }
        if(requisitos==null || requisitos.isEmpty()){
            throw new IllegalArgumentException("Requisitos no puede ser nulo o vacio");
        }
        if (fechaInicio == null) {
            throw new IllegalArgumentException("FechaInicio no puede ser nulo");
        }
        if (fechaFin == null) {
            throw new IllegalArgumentException("FechaFin no puede ser nulo");
        }
        if (maxPostulaciones != null && maxPostulaciones < 1) {
            throw new IllegalArgumentException("maxPostulaciones debe ser mayor que 0");
        }
    }
}
