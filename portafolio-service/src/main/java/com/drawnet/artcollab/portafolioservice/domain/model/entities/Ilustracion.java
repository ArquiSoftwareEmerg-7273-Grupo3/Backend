package com.drawnet.artcollab.portafolioservice.domain.model.entities;

import com.drawnet.artcollab.portafolioservice.domain.model.aggregates.Portafolio;
import com.drawnet.artcollab.portafolioservice.domain.model.valueobjects.Calificacion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
public class Ilustracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ilustradorId; // ID del ilustrador que creó la ilustración
    private String titulo;
    private String descripcion;
    private String urlImagen;
    private LocalDateTime fecha;

    private boolean publicada;

    @JsonBackReference
    @ManyToMany(mappedBy = "ilustraciones")
    private Set<Portafolio> portafolios = new HashSet<>();

    @OneToMany(mappedBy = "ilustracion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calificacion> calificaciones = new ArrayList<>();

    public Ilustracion() {}

    public Ilustracion(Long ilustradorId, String titulo, String descripcion, String urlImagen) {
        this.ilustradorId = ilustradorId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
        this.fecha = LocalDateTime.now(); // Se asigna la fecha actual al crear
        this.publicada = false;
    }

    public void agregarCalificacion(Long usuarioId, int puntuacion, String comentario) {
        this.calificaciones.add(new Calificacion(usuarioId, puntuacion, comentario, this));
    }

    public void setPortafolio(Portafolio portafolio) {
        this.portafolios.add(portafolio);
    }

    public void publicar() {
        this.publicada = true;
    }

    public boolean isPublicada() {
        return publicada;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Long getId() {
        return id;
    }

    public Long getIlustradorId() {
        return ilustradorId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Set<Portafolio> getPortafolios() {
        return portafolios;
    }

    public List<Calificacion> getCalificaciones() {
        return calificaciones;
    }
}
