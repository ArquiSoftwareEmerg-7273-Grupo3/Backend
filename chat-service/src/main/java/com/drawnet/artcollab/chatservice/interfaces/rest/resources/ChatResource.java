package com.drawnet.artcollab.chatservice.interfaces.rest.resources;

public record ChatResource(
        Long id,
        Long usuario1id,
        Long usuario2id,
        Boolean estado
) {}
