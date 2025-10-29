package com.drawnet.artcollab.chatservice.domain.model;

import com.drawnet.artcollab.chatservice.domain.model.commands.CrearChatCommand;
import com.drawnet.artcollab.chatservice.domain.model.commands.EnviarMensajeCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChatTest {

    @Test
    void crearChatCommand_deberiaCrearCorrectamente() {
        // Arrange
        Long usuario1id = 1L;
        Long usuario2id = 2L;

        // Act
        CrearChatCommand command = new CrearChatCommand(usuario1id, usuario2id);

        // Assert
        assertEquals(1L, command.usuario1id());
        assertEquals(2L, command.usuario2id());
    }

    @Test
    void crearChatCommand_conUsuario1IdNuloDebeLanzarExcepcion() {
        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new CrearChatCommand(null, 2L);
        });
        assertEquals("usuario1id es obligatorio", exception.getMessage());
    }

    @Test
    void crearChatCommand_conUsuario2IdNuloDebeLanzarExcepcion() {
        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new CrearChatCommand(1L, null);
        });
        assertEquals("usuario2id es obligatorio", exception.getMessage());
    }

    @Test
    void enviarMensajeCommand_deberiaCrearCorrectamente() {
        // Arrange
        Long chatId = 1L;
        Long remitenteId = 1L;
        String texto = "Hola";

        // Act
        EnviarMensajeCommand command = new EnviarMensajeCommand(chatId, remitenteId, texto);

        // Assert
        assertEquals(1L, command.chatId());
        assertEquals(1L, command.remitenteId());
        assertEquals("Hola", command.texto());
    }

    @Test
    void enviarMensajeCommand_conTextoNuloDebeLanzarExcepcion() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new EnviarMensajeCommand(1L, 1L, null);
        });
        assertEquals("texto es obligatorio", exception.getMessage());
    }

}
