package com.drawnet.artcollab.portafolioservice.domain.model.aggregates;

import com.drawnet.artcollab.portafolioservice.domain.model.entities.Categoria;
import com.drawnet.artcollab.portafolioservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate Root: Portafolio
 * Cada ilustrador tiene UN SOLO portafolio donde organiza sus ilustraciones en categorías
 */
@Getter
@Entity
@Table(
    name = "portafolios",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_portafolio_ilustrador",
            columnNames = {"id_ilustrador"}
        )
    }
)
public class Portafolio extends AuditableAbstractAggregateRoot<Portafolio> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_ilustrador", nullable = false, unique = true)
    private Long ilustradorId;
    
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @Column(length = 1000)
    private String descripcion;
    
    @Column(name = "url_imagen", length = 500)
    private String urlImagen;

    // Relación con Categorías (un portafolio tiene muchas categorías)
    @JsonManagedReference
    @OneToMany(mappedBy = "portafolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Categoria> categorias = new ArrayList<>();

    public Portafolio() {
    }

    public Portafolio(Long ilustradorId, String titulo, String descripcion, String urlImagen) {
        this.ilustradorId = ilustradorId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }

    // Métodos de negocio para gestión de categorías
    public void agregarCategoria(Categoria categoria) {
        categorias.add(categoria);
        categoria.setPortafolio(this);
    }
    
    public void removerCategoria(Categoria categoria) {
        categorias.remove(categoria);
        categoria.setPortafolio(null);
    }
    
    public int getCantidadCategorias() {
        return categorias.size();
    }
    
    public int getCantidadTotalIlustraciones() {
        return categorias.stream()
                .mapToInt(Categoria::getCantidadIlustraciones)
                .sum();
    }
    
    public boolean tieneCategorias() {
        return !categorias.isEmpty();
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

    public void setIlustradorId(Long ilustradorId) {
        this.ilustradorId = ilustradorId;
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

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public String getUrlImagen() {
        return urlImagen;
    }
}
