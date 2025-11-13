package com.drawnet.artcollab.CollaborativeProjects.domain.model.events;

import org.springframework.context.ApplicationEvent;

public class PostulacionAprobadaEvent extends ApplicationEvent {
    
    private final Long postulacionId;
    private final Long proyectoId;
    private final Long ilustradorId;
    private final String respuesta;

    public PostulacionAprobadaEvent(Object source, Long postulacionId, Long proyectoId, 
                                   Long ilustradorId, String respuesta) {
        super(source);
        this.postulacionId = postulacionId;
        this.proyectoId = proyectoId;
        this.ilustradorId = ilustradorId;
        this.respuesta = respuesta;
    }

    public Long getPostulacionId() {
        return postulacionId;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public Long getIlustradorId() {
        return ilustradorId;
    }

    public String getRespuesta() {
        return respuesta;
    }
}
