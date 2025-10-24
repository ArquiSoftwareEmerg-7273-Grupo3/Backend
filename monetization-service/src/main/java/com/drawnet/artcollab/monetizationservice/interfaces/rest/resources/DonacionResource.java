package com.drawnet.artcollab.monetizationservice.interfaces.rest.resources;

public record DonacionResource(Long id, Long donanteId, Long receptorId, Double monto) {
}
