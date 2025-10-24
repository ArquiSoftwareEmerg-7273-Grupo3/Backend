package com.drawnet.artcollab.profiles.interfaces.rest.resources;

import com.drawnet.artcollab.iam.domain.model.aggregates.User;

public record EscritorResource(Long id, String fullName, String biografia, String foto, String redes, Long suscripcion) {}
