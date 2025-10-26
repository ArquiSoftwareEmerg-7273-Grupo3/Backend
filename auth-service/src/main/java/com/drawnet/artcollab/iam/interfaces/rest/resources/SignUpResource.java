package com.drawnet.artcollab.iam.interfaces.rest.resources;


import java.time.LocalDate;
import java.util.Map;

public record SignUpResource(String username, String password, String ubicacion, String nombres, String apellidos, String telefono, String foto, String descripcion, LocalDate fechaNacimiento, Map<String, String> redesSociales) {
}