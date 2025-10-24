package com.drawnet.artcollab.monetizationservice.domain.model.aggregates;

import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearMonederoCommand;
import com.drawnet.artcollab.monetizationservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Entity;

@Entity
public class Monedero extends AuditableAbstractAggregateRoot<Monedero> {

    private Long usuarioId;
    private Double saldo;

    public Monedero(CrearMonederoCommand command) {
        this.usuarioId = command.usuarioId();
        this.saldo = 0.00;
    }

    public Monedero() {
    }

    public void comprarMonedas(Double monto) {
        if (monto <= 0 ) {
            throw new IllegalArgumentException("Inserte un saldo positivo");
        }
        double monedas = Math.floor(monto / 3);
        this.saldo += monedas;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void restarSaldo(Double monto) {
        if (this.saldo < monto) throw new IllegalArgumentException("Saldo insuficiente");
        this.saldo -= monto;
    }

    public void sumarSaldo(Double monto) {
        this.saldo += monto;
    }
}