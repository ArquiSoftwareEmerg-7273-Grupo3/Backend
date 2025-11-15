package com.drawnet.artcollab.CollaborativeProjects.domain.model.entities;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreatePostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
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
    private String estado;

    @NotNull
    private Date fecha;

    public Postulacion() {
    }

    public Postulacion(CreatePostulacionCommand command) {
        this();
        this.proyectoId = command.proyectoId();
        this.ilustradorId = command.ilustradorId();
        this.estado = command.estado();
        this.fecha = command.fecha();
    }

    public Long getId() {
        return id;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public String getEstado() {
        return estado;
    }

    public Long getIlustradorId() {
        return ilustradorId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setEstado(String estado) {
        if (!"CONFIRMADO".equalsIgnoreCase(estado) && !"RECHAZADO".equalsIgnoreCase(estado) && !"EN ESPERA".equalsIgnoreCase(estado)) {
            throw new IllegalArgumentException("Estado inválido. Solo se permite CONFIRMADO, RECHAZADO o EN ESPERA.");
        }
        this.estado = estado.toUpperCase();
    }
}
