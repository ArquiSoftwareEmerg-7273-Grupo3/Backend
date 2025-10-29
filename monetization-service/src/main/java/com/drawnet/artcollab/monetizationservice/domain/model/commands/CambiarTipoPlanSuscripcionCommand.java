package com.drawnet.artcollab.monetizationservice.domain.model.commands;

import com.drawnet.artcollab.monetizationservice.domain.model.valueobjects.Plan;

public record CambiarTipoPlanSuscripcionCommand(Long usuarioId,Plan tipo) {
}
