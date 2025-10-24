package com.drawnet.artcollab.domain.model;

import com.drawnet.artcollab.iam.domain.model.commands.SignInCommand;
import com.drawnet.artcollab.iam.domain.model.commands.SignUpCommand;
import com.drawnet.artcollab.iam.domain.model.entities.Role;
import com.drawnet.artcollab.iam.domain.model.valueobjects.Roles;
import com.drawnet.artcollab.profiles.domain.model.commands.CreateEscritorCommand;
import com.drawnet.artcollab.profiles.domain.model.commands.CreateIlustradorCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IamTest {
    //User

    @Test
    void crearSignInCommand_deberiaCrearCorrectamente() {
        // Arrange
        String username = "jmontes";
        String password = "securePassword123";

        // Act
        SignInCommand command = new SignInCommand(username, password);

        // Assert
        assertEquals("jmontes", command.username());
        assertEquals("securePassword123", command.password());
    }

    @Test
    void crearSignInCommand_conUsernameNuloDebeLanzarExcepcion() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SignInCommand(null, "securePassword123");
        });
        assertEquals("El nombre de usuario es obligatorio", exception.getMessage());
    }

    @Test
    void crearSignInCommand_conPasswordVacioDebeLanzarExcepcion() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SignInCommand("jmontes", "");
        });
        assertEquals("La contraseña es obligatoria", exception.getMessage());
    }

    // Pruebas para SignUpCommand
    @Test
    void crearSignUpCommand_deberiaCrearCorrectamente() {
        // Arrange
        String username = "jmontes";
        String password = "securePassword123";
        Role role = new Role(Roles.ESCRITOR);

        // Act
        SignUpCommand command = new SignUpCommand(username, password, role);

        // Assert
        assertEquals("jmontes", command.username());
        assertEquals("securePassword123", command.password());
        assertEquals(role, command.role());
    }

    @Test
    void crearSignUpCommand_conRoleNuloDebeLanzarExcepcion() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SignUpCommand("jmontes", "securePassword123", null);
        });
        assertEquals("El rol es obligatorio", exception.getMessage());
    }

    //Escritor
    @Test
    void crearEscritor() {
        // Arrange
        String firstName = "Juan";
        String lastName = "Pérez";
        String biografia = "Escritor apasionado por la literatura.";
        String foto = "foto_url";
        String redes = "@juanperez";
        Long suscripcion = 1L;
        Long userId = 123L;

        // Act
        CreateEscritorCommand command = new CreateEscritorCommand(firstName, lastName, biografia, foto, redes, suscripcion, userId);

        // Assert
        assertEquals("Juan", command.firstName());
        assertEquals("Pérez", command.lastName());
        assertEquals("Escritor apasionado por la literatura.", command.biografia());
        assertEquals("foto_url", command.foto());
        assertEquals("@juanperez", command.redes());
        assertEquals(1L, command.suscripcion());
        assertEquals(123L, command.userId());

    }
    @Test
    void crearEscritorCommand_conNombreNuloDebeLanzarExcepcion() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CreateEscritorCommand(null, "Pérez", "Biografía", "foto_url", "@redes", 1L, 123L);
        });
        assertEquals("El nombre es obligatorio", exception.getMessage());
    }

    @Test
    void crearEscritorCommand_conNombreVacioDebeLanzarExcepcion() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CreateEscritorCommand(" ", "Pérez", "Biografía", "foto_url", "@redes", 1L, 123L);
        });
        assertEquals("El nombre es obligatorio", exception.getMessage());
    }
    //Ilustrador

    @Test
    void crearIlustradorCommand_deberiaCrearCorrectamente() {
        // Arrange
        String firstName = "Carlos";
        String lastName = "Ramírez";
        String biografia = "Ilustrador experto en diseño digital.";
        String foto = "foto_url";
        String redes = "@carlosramirez";
        Long suscripcion = 2L;
        Long userId = 456L;

        // Act
        CreateIlustradorCommand command = new CreateIlustradorCommand(firstName, lastName, biografia, foto, redes, suscripcion, userId);

        // Assert
        assertEquals("Carlos", command.firstName());
        assertEquals("Ramírez", command.lastName());
        assertEquals("Ilustrador experto en diseño digital.", command.biografia());
        assertEquals("foto_url", command.foto());
        assertEquals("@carlosramirez", command.redes());
        assertEquals(2L, command.suscripcion());
        assertEquals(456L, command.userId());
    }

    @Test
    void crearIlustradorCommand_conNombreNuloDebeLanzarExcepcion() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CreateIlustradorCommand(null, "Ramírez", "Biografía", "foto_url", "@redes", 2L, 456L);
        });
        assertEquals("El nombre es obligatorio", exception.getMessage());
    }

    @Test
    void crearIlustradorCommand_conNombreVacioDebeLanzarExcepcion() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CreateIlustradorCommand(" ", "Ramírez", "Biografía", "foto_url", "@redes", 2L, 456L);
        });
        assertEquals("El nombre es obligatorio", exception.getMessage());
    }

}
