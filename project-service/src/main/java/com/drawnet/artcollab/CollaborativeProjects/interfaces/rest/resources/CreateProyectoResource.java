package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

import java.util.Date;

public record CreateProyectoResource(String titulo, String descripcion, String urlImagen, Date fecha) {
}
