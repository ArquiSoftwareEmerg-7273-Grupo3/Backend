package com.drawnet.artcollab.profiles.domain.model.aggregates;


import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.profiles.domain.model.commands.CreateIlustradorCommand;
import com.drawnet.artcollab.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Ilustrador extends AuditableAbstractAggregateRoot<Ilustrador> {

    // Relación bidireccional con User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "nombre_artistico")
    private String nombreArtistico;

    @Column(name = "subscripcion")
    private Boolean subscripcion = false;  // Siempre inicia en false

    public Ilustrador() {}

    public Ilustrador(CreateIlustradorCommand command, User user) {
        this.nombreArtistico = command.nombreArtistico();
        this.subscripcion = false; // Siempre inicia en false
        this.user = user;
    }

    public String getNombreArtistico() {
        return nombreArtistico;
    }

    public Boolean getSubscripcion() {
        return subscripcion;
    }

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    // Método para activar suscripción
    public void activarSubscripcion() {
        this.subscripcion = true;
    }

    // Método para desactivar suscripción
    public void desactivarSubscripcion() {
        this.subscripcion = false;
    }
}
