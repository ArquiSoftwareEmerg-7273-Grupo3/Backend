package com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects;

public enum ModalidadProyecto {
    REMOTO("Remoto"),
    PRESENCIAL("Presencial"),
    HIBRIDO("Híbrido");

    private final String tipo;
    
    ModalidadProyecto(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}