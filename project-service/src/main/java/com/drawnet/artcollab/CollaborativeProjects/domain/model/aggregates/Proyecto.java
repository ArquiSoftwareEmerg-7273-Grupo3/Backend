package com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreateProyectoCommand;

import com.drawnet.artcollab.CollaborativeProjects.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects.EstadoProyecto;

@Entity

public class Proyecto extends AuditableAbstractAggregateRoot<Proyecto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "escritor_id", nullable = false)
    private Long escritorId;

    @NotNull
    @Column(nullable = false, length = 200)
    private String titulo;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(precision = 10, scale = 2)
    private BigDecimal presupuesto;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoProyecto estado;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "max_postulaciones")
    private Integer maxPostulaciones;

    @Column(name = "ilustrador_asignado_id")
    private Long ilustradorAsignadoId; // Ilustrador seleccionado

    protected Proyecto() {}

    public Proyecto(CreateProyectoCommand command) {
        this.escritorId = command.escritorId();
        this.titulo = command.titulo();
        this.descripcion = command.descripcion();
        this.presupuesto = command.presupuesto();
        this.estado = EstadoProyecto.ABIERTO;
        this.fechaInicio = command.fechaInicio();
        this.fechaFin = command.fechaFin();
        this.maxPostulaciones = command.maxPostulaciones() != null ? command.maxPostulaciones() : 50;
    }

    public boolean perteneceA(Long escritorId) {
        return this.escritorId.equals(escritorId);
    }

    public boolean estaAbierto() {
        return this.estado == EstadoProyecto.ABIERTO;
    }

    public void cerrar() {
        if (this.estado == EstadoProyecto.FINALIZADO) {
            throw new IllegalStateException("No se puede cerrar un proyecto finalizado");
        }
        this.estado = EstadoProyecto.CERRADO;
    }

    public void iniciar(Long ilustradorId) {
        if (this.estado != EstadoProyecto.ABIERTO) {
            throw new IllegalStateException("Solo se pueden iniciar proyectos abiertos");
        }
        this.estado = EstadoProyecto.EN_PROGRESO;
        this.ilustradorAsignadoId = ilustradorId;
    }

    public void finalizar() {
        if (this.estado != EstadoProyecto.EN_PROGRESO) {
            throw new IllegalStateException("Solo se pueden finalizar proyectos en progreso");
        }
        this.estado = EstadoProyecto.FINALIZADO;
    }

    public void reabrir() {
        if (this.estado == EstadoProyecto.FINALIZADO) {
            throw new IllegalStateException("No se puede reabrir un proyecto finalizado");
        }
        this.estado = EstadoProyecto.ABIERTO;
        this.ilustradorAsignadoId = null;
    }

    public Long getId() { return id; }
    public Long getEscritorId() { return escritorId; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPresupuesto() { return presupuesto; }
    public EstadoProyecto getEstado() { return estado; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public Integer getMaxPostulaciones() { return maxPostulaciones; }
    public Long getIlustradorAsignadoId() { return ilustradorAsignadoId; }
}
