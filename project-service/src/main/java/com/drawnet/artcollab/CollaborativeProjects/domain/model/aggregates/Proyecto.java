package com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreateProyectoCommand;

import com.drawnet.artcollab.CollaborativeProjects.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Date;

@Entity

public class Proyecto extends AuditableAbstractAggregateRoot<Proyecto> {

    @NotNull
    @Column(name = "id_escritor")
    private Long escritorId;

    @NotNull
    private String titulo;

    @NotNull
    private String descripcion;

    @NotNull
    @Column(name = "url_imagen")
    private String urlImagen;

    @NotNull
    private Date fecha;

    public Proyecto() {
    }

    public Proyecto(CreateProyectoCommand command) {
        this.escritorId = command.escritorId();
        this.titulo = command.titulo();
        this.descripcion = command.descripcion();
        this.urlImagen = command.urlImagen();
        this.fecha = command.fecha();
    }

    public Long getEscritorId() {
        return escritorId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getFecha() {
        return fecha;
    }
}
