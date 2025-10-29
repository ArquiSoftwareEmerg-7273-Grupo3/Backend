package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

import java.util.Date;

public record PostulacionResource(Long id, Long proyectoId, Long ilustradorId, String estado, Date fecha) {
}
