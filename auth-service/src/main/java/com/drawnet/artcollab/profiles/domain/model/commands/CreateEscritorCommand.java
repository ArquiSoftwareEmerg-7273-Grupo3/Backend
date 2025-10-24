package com.drawnet.artcollab.profiles.domain.model.commands;

public record CreateEscritorCommand(String firstName, String lastName, String biografia, String foto, String redes, Long suscripcion, Long userId){
    public CreateEscritorCommand {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
    }
}
