package com.drawnet.artcolab.libroservice.domain.model.aggregates;

import com.drawnet.artcolab.libroservice.domain.model.valueobjects.Calificacion;
import com.drawnet.artcolab.libroservice.interfaces.rest.resources.LibroResource;
import com.drawnet.artcolab.libroservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Libro extends AuditableAbstractAggregateRoot<Libro> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long escritorId;

    private String titulo;

    private String sinopsis;

    private String urlImagen;

    private LocalDateTime fecha;

    private String urlLibro;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calificacion> calificaciones = new ArrayList<>();

    public Libro() {}

    public Libro(Long escritorId, String titulo, String sinopsis, String urlImagen, String urlLibro) {
        this.escritorId = escritorId;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.urlImagen = urlImagen;
        this.urlLibro = urlLibro;
        this.fecha = LocalDateTime.now();
    }

    public void agregarCalificacion(Long usuarioId, int puntuacion, String comentario) {
        this.calificaciones.add(new Calificacion(usuarioId, puntuacion, comentario, this));
    }

    public List<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public Long getId() {
        return id;
    }

    public Long getEscritorId() {
        return escritorId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getUrlLibro() {
        return urlLibro;
    }
}
