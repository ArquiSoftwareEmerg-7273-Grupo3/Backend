package com.drawnet.artcollab.monetizationservice.domain.model.commands;


public record CrearDonacionCommand(Long donanteId, Long receptorId, Double monto) {

    public CrearDonacionCommand {
        if (donanteId == null || receptorId == null || monto == null) {
            throw new IllegalArgumentException("Los campos no pueden ser nulos");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
    }
}
