package com.drawnet.artcolab.libroservice.interfaces.rest.resources;

import java.util.Date;

public record LibroResource(
    Long id,
    String titulo,
    String sinopsis,
    String urlImagen,
    String urlLibro
) {
}
