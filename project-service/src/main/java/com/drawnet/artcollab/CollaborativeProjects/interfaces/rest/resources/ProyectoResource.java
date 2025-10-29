package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

import java.util.Date;

public record ProyectoResource(Long id, Long escritorId, String titulo, String descripcion, String urlImagen, Date fecha) {

}
