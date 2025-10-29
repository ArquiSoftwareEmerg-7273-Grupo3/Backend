package com.drawnet.artcollab.portafolioservice.domain.model.aggregates;

import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.portafolioservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "portafolios")
public class Portafolio extends AuditableAbstractAggregateRoot<Portafolio> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_ilustrador", nullable = false)
    private Long ilustradorId;
    private String titulo;
    private String descripcion;
    private String urlImagen;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "portafolios_ilustraciones",
            joinColumns = @JoinColumn(name = "id_portafolio"),
            inverseJoinColumns = @JoinColumn(name = "id_ilustracion")
    )
    private Set<Ilustracion> ilustraciones = new HashSet<>();

    public Portafolio() {
    }

    public Portafolio(Long ilustradorId, String titulo, String descripcion, String urlImagen) {
        this.ilustradorId = ilustradorId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }

    public void agregarIlustracion(Ilustracion ilustracion) {
        ilustraciones.add(ilustracion);
        ilustracion.getPortafolios().add(this);
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

    public Set<Ilustracion> getIlustraciones() {
        return ilustraciones;
    }

    public String getUrlImagen() {
        return urlImagen;
    }
}
