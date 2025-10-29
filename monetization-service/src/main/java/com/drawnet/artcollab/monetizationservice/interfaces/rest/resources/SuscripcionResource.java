package com.drawnet.artcollab.monetizationservice.interfaces.rest.resources;

import java.time.LocalDate;

public record SuscripcionResource(Long id,
                                  Long usuarioId,
                                  LocalDate fechaInicio,
                                  LocalDate fechaFin,
                                  String tipo) {
}
