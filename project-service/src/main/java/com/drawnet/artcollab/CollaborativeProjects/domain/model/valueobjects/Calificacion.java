package com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects;


import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Ilustracion;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;

    @Column(nullable = false)
    private int puntuacion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_ilustracion")
    private Ilustracion ilustracion;

    protected Calificacion() {
        // Constructor protegido para JPA
    }

    public Calificacion(Long usuarioId, int puntuacion, String comentario, Ilustracion ilustracion) {
        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5.");
        }
        this.usuarioId = usuarioId;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.fecha = LocalDateTime.now();
        this.ilustracion = ilustracion;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
