package com.drawnet.artcollab.profiles.domain.model.aggregates;


import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.profiles.domain.model.commands.CreateEscritorCommand;
import com.drawnet.artcollab.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Escritor extends AuditableAbstractAggregateRoot<Escritor> {

    // Relación bidireccional con User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "razon_social")
    private String razonSocial; // no obligatorio

    @Column(name = "ruc")
    private String ruc; // no obligatorio

    @Column(name = "nombre_comercial")
    private String nombreComercial; // no obligatorio

    @Column(name = "sitio_web")
    private String sitioWeb; // no obligatorio

    @Column(name = "logo")
    private String logo; // no obligatorio

    @Column(name = "ubicacion_empresa")
    private String ubicacionEmpresa; // no obligatorio

    @Column(name = "tipo_empresa")
    private String tipoEmpresa; // no obligatorio

    public Escritor(){}

    public Escritor(CreateEscritorCommand command, User user) {
        this.razonSocial = command.razonSocial();
        this.ruc = command.ruc();
        this.nombreComercial = command.nombreComercial();
        this.sitioWeb = command.sitioWeb();
        this.logo = command.logo();
        this.ubicacionEmpresa = command.ubicacionEmpresa();
        this.tipoEmpresa = command.tipoEmpresa();
        this.user = user;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public String getLogo() {
        return logo;
    }

    public String getUbicacionEmpresa() {
        return ubicacionEmpresa;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}


