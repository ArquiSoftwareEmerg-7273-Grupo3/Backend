package com.drawnet.artcollab.portafolioservice.domain.model.entities;

import com.drawnet.artcollab.portafolioservice.domain.model.aggregates.Portafolio;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidad Categoria - Representa una subcarpeta dentro de un Portafolio
 * para organizar ilustraciones por temas o tipos
 */
@Getter
@Entity
@Table(name = "categorias")
@NoArgsConstructor
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(nullable = false)
    private Integer orden = 0; // Para ordenar las categorías en el portafolio
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Relación con Portafolio (muchas categorías pertenecen a un portafolio)
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portafolio_id", nullable = false)
    private Portafolio portafolio;
    
    // Relación con Ilustraciones (una categoría tiene muchas ilustraciones)
    @JsonManagedReference
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ilustracion> ilustraciones = new ArrayList<>();
    
    // Constructor
    public Categoria(String nombre, String descripcion, Portafolio portafolio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.portafolio = portafolio;
        this.orden = 0;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public Categoria(String nombre, String descripcion, Integer orden, Portafolio portafolio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.orden = orden;
        this.portafolio = portafolio;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        fechaActualizacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    // Métodos de negocio
    public void agregarIlustracion(Ilustracion ilustracion) {
        ilustraciones.add(ilustracion);
        ilustracion.setCategoria(this);
    }
    
    public void removerIlustracion(Ilustracion ilustracion) {
        ilustraciones.remove(ilustracion);
        ilustracion.setCategoria(null);
    }
    
    public int getCantidadIlustraciones() {
        return ilustraciones.size();
    }
    
    public boolean tieneIlustraciones() {
        return !ilustraciones.isEmpty();
    }
    
    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public void setOrden(Integer orden) {
        this.orden = orden;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public void setPortafolio(Portafolio portafolio) {
        this.portafolio = portafolio;
    }
    
    // Getters explícitos para mejor control
    public Long getId() {
        return id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public Integer getOrden() {
        return orden;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public Portafolio getPortafolio() {
        return portafolio;
    }
    
    public List<Ilustracion> getIlustraciones() {
        return ilustraciones;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(id, categoria.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", orden=" + orden +
                ", cantidadIlustraciones=" + ilustraciones.size() +
                '}';
    }
}
