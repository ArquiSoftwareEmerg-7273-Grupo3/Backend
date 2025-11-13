package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

import java.time.LocalDateTime;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects.EstadoPostulacion;

public record PostulacionResource(Long id, Long proyectoId, Long ilustradorId, EstadoPostulacion estado, LocalDateTime fechaPostulacion) {
}
