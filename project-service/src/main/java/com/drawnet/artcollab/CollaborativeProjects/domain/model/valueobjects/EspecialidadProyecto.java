package com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects;

public enum EspecialidadProyecto {
    ILUSTRACION_DIGITAL("Ilustración Digital"),
    ILUSTRACION_TRADICIONAL("Ilustración Tradicional"),
    CONCEPT_ART("Concept Art"),
    COMIC_MANGA("Cómic/Manga"),
    ANIMACION("Animación"),
    ARTE_3D("Arte 3D"),
    ARTE_VECTORIAL("Arte Vectorial");

    private final String tipo;

    EspecialidadProyecto(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
