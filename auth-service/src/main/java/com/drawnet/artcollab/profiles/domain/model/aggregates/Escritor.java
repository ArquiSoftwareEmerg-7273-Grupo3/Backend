package com.drawnet.artcollab.profiles.domain.model.aggregates;


import com.drawnet.artcollab.iam.domain.model.aggregates.User;

import com.drawnet.artcollab.profiles.domain.model.commands.CreateEscritorCommand;
import com.drawnet.artcollab.profiles.domain.model.valueobjects.PersonName;
import com.drawnet.artcollab.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Escritor extends AuditableAbstractAggregateRoot<Escritor> {

    private Long userId;

    @Embedded
    private PersonName nombre;

    private String biografia;

    @Column(name = "foto_perfil")
    private String foto;

    @Column(name = "redes_sociales")
    private String redes;

    private Long suscripcion;

    public Escritor(){}

    public Escritor(CreateEscritorCommand command) {
        this.nombre = new PersonName(command.firstName(), command.lastName());
        this.biografia = command.biografia();
        this.foto = command.foto();
        this.redes = command.redes();
        this.suscripcion = command.suscripcion();
        this.userId = command.userId();
    }

    public String getFullName() {
        return nombre.getFullName();
    }


    public String getBiografia() {
        return biografia;
    }

    public String getFoto() {
        return foto;
    }

    public String getRedes() {
        return redes;
    }

    public Long getSuscripcion() {
        return suscripcion;
    }
}


