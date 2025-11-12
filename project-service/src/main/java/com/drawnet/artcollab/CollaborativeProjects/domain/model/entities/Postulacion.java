package com.drawnet.artcollab.CollaborativeProjects.domain.model.entities;

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

    protected Postulacion() {}

    public Postulacion(CreatePostulacionCommand command) {
        this.proyectoId = command.proyectoId();
        this.ilustradorId = command.ilustradorId();
        this.estado = EstadoPostulacion.EN_ESPERA;
        this.mensaje = command.mensaje();
        this.fechaPostulacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public EstadoPostulacion getEstado() {
        return estado;
    }

    public Long getIlustradorId() {
        return ilustradorId;
    }

    public LocalDateTime getFechaPostulacion() {
        return fechaPostulacion;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    // Métodos de negocio
    public void aprobar(String respuesta) {
        if (this.estado != EstadoPostulacion.EN_ESPERA) {
            throw new IllegalStateException("Solo se pueden aprobar postulaciones en espera");
        }
        this.estado = EstadoPostulacion.APROBADA;
        this.respuesta = respuesta;
        this.fechaRespuesta = LocalDateTime.now();
    }

    public void rechazar(String razon) {
        if (this.estado != EstadoPostulacion.EN_ESPERA) {
            throw new IllegalStateException("Solo se pueden rechazar postulaciones en espera");
        }
        this.estado = EstadoPostulacion.RECHAZADA;
        this.respuesta = razon;
        this.fechaRespuesta = LocalDateTime.now();
    }

    public void cancelar() {
        if (this.estado != EstadoPostulacion.EN_ESPERA) {
            throw new IllegalStateException("Solo se pueden cancelar postulaciones en espera");
        }
        this.estado = EstadoPostulacion.CANCELADA;
        this.fechaRespuesta = LocalDateTime.now();
    }

    public boolean isEnEspera() {
        return this.estado == EstadoPostulacion.EN_ESPERA;
    }

    public boolean isAprobada() {
        return this.estado == EstadoPostulacion.APROBADA;
    }
}
