package com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects;

public enum ContratoProyecto {
    TIEMPO_COMPLETO("Tiempo Completo"),
    MEDIO_TIEMPO("Medio Tiempo"),
    FREELANCE("Freelance"),
    TEMPORAL("Temporal"),
    PRACTICAS("Prácticas"),
    CONTRATO("Por Contrato"),
    VOLUNTARIADO("Voluntariado");

    private final String tipo;

    ContratoProyecto(String tipo){
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }


}
