package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateProyectoResource(String titulo, String descripcion, BigDecimal presupuesto,
									  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @Schema(type = "string", example = "2025-11-12T17:12:04", description = "Fecha y hora del inicio") LocalDateTime fechaInicio,
									  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @Schema(type = "string", example = "2025-11-12T17:12:04", description = "Fecha y hora del fin") LocalDateTime fechaFin,
									  @Schema(type = "integer", example = "5", description = "Número máximo de postulaciones") Integer maxPostulaciones) {
}
