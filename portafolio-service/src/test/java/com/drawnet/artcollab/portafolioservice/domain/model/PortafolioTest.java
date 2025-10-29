package com.drawnet.artcollab.portafolioservice.domain.model;

import com.drawnet.artcollab.portafolioservice.domain.model.commands.CrearPortafolioCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortafolioTest {
    @Test
    void crearPortafolioCommand_deberiaCrearCorrectamente() {
        // Arrange
        Long ilustradorId = 1L;
        String titulo = "Mi Portafolio";
        String descripcion = "Descripción del portafolio";
        String urlImagen = "http://imagen.com/portafolio.jpg";

        // Act
        CrearPortafolioCommand command = new CrearPortafolioCommand(ilustradorId, titulo, descripcion, urlImagen);

        // Assert
        assertEquals(ilustradorId, command.ilustradorId());
        assertEquals(titulo, command.titulo());
        assertEquals(descripcion, command.descripcion());
        assertEquals(urlImagen, command.urlImagen());
    }
}
