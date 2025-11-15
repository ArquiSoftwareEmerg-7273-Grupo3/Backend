package com.drawnet.artcollab.domain.model;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreateProyectoCommand;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProyectoTest {

    @Test
    void createProyectoCommand_deberiaCrearCorrectamente() {
        // Arrange
        Long escritorId = 1L;
        String titulo = "Mi Proyecto";
        String descripcion = "Descripción del proyecto";
        String urlImagen = "http://imagen.com/proyecto.jpg";
        Date fecha = new Date();

        // Act
        CreateProyectoCommand command = new CreateProyectoCommand(escritorId, titulo, descripcion, urlImagen, fecha);

        // Assert
        assertEquals(escritorId, command.escritorId());
        assertEquals(titulo, command.titulo());
        assertEquals(descripcion, command.descripcion());
        assertEquals(urlImagen, command.urlImagen());
        assertEquals(fecha, command.fecha());
    }

}
