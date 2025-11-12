package com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects;

public enum EstadoPostulacion {
    EN_ESPERA("En espera de revisión"),
    APROBADA("Aprobada por el escritor"),
    RECHAZADA("Rechazada por el escritor"),
    CANCELADA("Cancelada por el ilustrador");

    private final String descripcion;

    EstadoPostulacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static EstadoPostulacion fromString(String estado) {
        for (EstadoPostulacion e : EstadoPostulacion.values()) {
            if (e.name().equalsIgnoreCase(estado)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + estado);
    }
}
