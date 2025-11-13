package com.drawnet.artcollab.CollaborativeProjects.domain.model.events;

import org.springframework.context.ApplicationEvent;

public class PostulacionCreadaEvent extends ApplicationEvent {
    
    private final Long postulacionId;
    private final Long proyectoId;
    private final Long ilustradorId;
    private final Long escritorId;
    private final String mensaje;

    public PostulacionCreadaEvent(Object source, Long postulacionId, Long proyectoId, 
                                 Long ilustradorId, Long escritorId, String mensaje) {
        super(source);
        this.postulacionId = postulacionId;
        this.proyectoId = proyectoId;
        this.ilustradorId = ilustradorId;
        this.escritorId = escritorId;
        this.mensaje = mensaje;
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

    public Long getEscritorId() {
        return escritorId;
    }

    public String getMensaje() {
        return mensaje;
    }
}
