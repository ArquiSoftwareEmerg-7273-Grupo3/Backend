package com.drawnet.artcollab.monetizationservice.domain.model.aggregates;

import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearDonacionCommand;
import com.drawnet.artcollab.monetizationservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "donaciones")
public class Donacion extends AuditableAbstractAggregateRoot<Donacion> {

    @Column(name = "id_donante", nullable = false)
    private Long donanteId;

    @Column(name = "id_receptor", nullable = false)
    private Long receptorId;

    private Double monto;

    public Donacion() {}

    public Donacion(CrearDonacionCommand command) {
        this.donanteId = command.donanteId();
        this.receptorId = command.receptorId();
        this.monto = command.monto();
    }

    public Long getDonanteId() {
        return donanteId;
    }

    public Long getReceptorId() {
        return receptorId;
    }

    public Double getMonto() {
        return monto;
    }
}
