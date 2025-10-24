package com.drawnet.artcolab.libroservice.interfaces.rest.resources;

public record CrearLibroResource(
    String titulo,
    String sinopsis,
    String urlImagen,
    String urlLibro
) {
}
