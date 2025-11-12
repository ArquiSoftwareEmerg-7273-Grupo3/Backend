package com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects;

public enum EstadoProyecto {
    ABIERTO("Abierto para postulaciones"),
    CERRADO("Cerrado, no acepta postulaciones"),
    EN_PROGRESO("En progreso con ilustrador asignado"),
    FINALIZADO("Proyecto finalizado");

    private final String descripcion;

    EstadoProyecto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
