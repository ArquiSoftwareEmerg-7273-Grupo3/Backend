package com.drawnet.artcollab.portafolioservice.domain.model.entities;

import com.drawnet.artcollab.portafolioservice.domain.model.valueobjects.Calificacion;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity: Ilustracion
 * Representa una ilustración que pertenece a una categoría específica
 */
@Getter
@Entity
@Table(name = "ilustraciones")
public class Ilustracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ilustrador_id", nullable = false)
    private Long ilustradorId; // ID del ilustrador que creó la ilustración
    
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @Column(length = 1000)
    private String descripcion;
    
    @Column(name = "url_imagen", nullable = false, length = 500)
    private String urlImagen;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private boolean publicada;

    // Relación con Categoría (muchas ilustraciones pertenecen a una categoría)
    // Relación con Categoría (muchas ilustraciones pertenecen a una categoría)
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @JsonIgnore
    @OneToMany(mappedBy = "ilustracion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Calificacion> calificaciones = new ArrayList<>();

    public Ilustracion() {}

    public Ilustracion(Long ilustradorId, String titulo, String descripcion, String urlImagen, Categoria categoria) {
        this.ilustradorId = ilustradorId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
        this.categoria = categoria;
        this.fecha = LocalDateTime.now();
        this.publicada = false;
    }

    public void agregarCalificacion(Long usuarioId, int puntuacion, String comentario) {
        this.calificaciones.add(new Calificacion(usuarioId, puntuacion, comentario, this));
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public List<Calificacion> getCalificaciones() {
        return calificaciones;
    }
}
