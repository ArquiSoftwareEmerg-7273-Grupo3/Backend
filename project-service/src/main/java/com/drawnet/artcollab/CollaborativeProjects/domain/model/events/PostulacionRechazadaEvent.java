package com.drawnet.artcollab.CollaborativeProjects.domain.model.events;

import org.springframework.context.ApplicationEvent;

public class PostulacionRechazadaEvent extends ApplicationEvent {
    
    private final Long postulacionId;
    private final Long proyectoId;
    private final Long ilustradorId;
    private final String razon;

    public PostulacionRechazadaEvent(Object source, Long postulacionId, Long proyectoId, 
                                    Long ilustradorId, String razon) {
        super(source);
        this.postulacionId = postulacionId;
        this.proyectoId = proyectoId;
        this.ilustradorId = ilustradorId;
        this.razon = razon;
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

    public String getRazon() {
        return razon;
    }
}
