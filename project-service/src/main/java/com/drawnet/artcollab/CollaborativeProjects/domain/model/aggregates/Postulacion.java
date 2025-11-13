package com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreatePostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects.EstadoPostulacion;

@Entity
@Getter
@Table(name = "postulaciones")
public class Postulacion extends AuditableAbstractAggregateRoot<Postulacion> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "id_proyecto")
    private Long proyectoId;

    @NotNull
    @Column(name = "id_ilustrador")
    private Long ilustradorId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoPostulacion estado;

    @Column(columnDefinition = "TEXT")
    private String mensaje; // Mensaje del ilustrador al postularse

    @Column(columnDefinition = "TEXT")
    private String respuesta; // Respuesta del escritor (razón de rechazo/aprobación)

    @NotNull
    @Column(name = "fecha_postulacion")
    private LocalDateTime fechaPostulacion;

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;
    
    @Column(name = "fecha_visualizacion")
    private LocalDateTime fechaVisualizacion; // Cuando el escritor vio la postulación

    protected Postulacion() {}

    public Postulacion(CreatePostulacionCommand command) {
        this.proyectoId = command.proyectoId();
        this.ilustradorId = command.ilustradorId();
        this.estado = EstadoPostulacion.EN_ESPERA;
        this.mensaje = command.mensaje();
        this.fechaPostulacion = LocalDateTime.now();
    }

    // Métodos de negocio
    public void aprobar(String respuesta, Long escritorId) {
        validarEstadoEnEspera();
        this.estado = EstadoPostulacion.APROBADA;
        this.respuesta = respuesta;
        this.fechaRespuesta = LocalDateTime.now();
    }

    public void rechazar(String razon, Long escritorId) {
        validarEstadoEnEspera();
        this.estado = EstadoPostulacion.RECHAZADA;
        this.respuesta = razon;
        this.fechaRespuesta = LocalDateTime.now();
    }

    public void cancelar(Long ilustradorId) {
        validarEstadoEnEspera();
        if (!this.ilustradorId.equals(ilustradorId)) {
            throw new IllegalStateException("Solo el ilustrador que creó la postulación puede cancelarla");
        }
        this.estado = EstadoPostulacion.CANCELADA;
        this.fechaRespuesta = LocalDateTime.now();
    }
    
    public void marcarComoVista() {
        if (this.fechaVisualizacion == null) {
            this.fechaVisualizacion = LocalDateTime.now();
        }
    }

    private void validarEstadoEnEspera() {
        if (this.estado != EstadoPostulacion.EN_ESPERA) {
            throw new IllegalStateException("Solo se pueden modificar postulaciones en espera");
        }
    }

    public boolean perteneceAIlustrador(Long ilustradorId) {
        return this.ilustradorId.equals(ilustradorId);
    }

    public boolean perteneceAProyecto(Long proyectoId) {
        return this.proyectoId.equals(proyectoId);
    }

    public boolean isEnEspera() {
        return this.estado == EstadoPostulacion.EN_ESPERA;
    }

    public boolean isAprobada() {
        return this.estado == EstadoPostulacion.APROBADA;
    }
    
    public boolean isRechazada() {
        return this.estado == EstadoPostulacion.RECHAZADA;
    }
    
    public boolean isCancelada() {
        return this.estado == EstadoPostulacion.CANCELADA;
    }
}
