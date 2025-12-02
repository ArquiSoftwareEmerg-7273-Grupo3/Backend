package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreatePostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CreatePostulacionResource;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CreatePostulacionCommandFromResourceAssembler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static CreatePostulacionCommand toCommandFromResource(Long proyectoId, Long ilustradorId, CreatePostulacionResource resource) {
        // Construir mensaje estructurado con toda la información
        StringBuilder mensaje = new StringBuilder();
        
        if (resource.coverLetter() != null && !resource.coverLetter().isEmpty()) {
            mensaje.append("Carta de presentación: ").append(resource.coverLetter()).append("\n\n");
        }
        
        if (resource.estimatedTime() != null && !resource.estimatedTime().isEmpty()) {
            mensaje.append("Tiempo estimado: ").append(resource.estimatedTime()).append("\n");
        }
        
        if (resource.proposedBudget() != null) {
            mensaje.append("Presupuesto propuesto: S/ ").append(resource.proposedBudget()).append("\n");
        }
        
        if (resource.portfolioLinks() != null && !resource.portfolioLinks().isEmpty()) {
            mensaje.append("\nEnlaces de portafolio:\n");
            for (String link : resource.portfolioLinks()) {
                mensaje.append("- ").append(link).append("\n");
            }
        }
        
        if (resource.answers() != null && !resource.answers().isEmpty()) {
            mensaje.append("\nRespuestas a preguntas:\n");
            resource.answers().forEach((pregunta, respuesta) -> 
                mensaje.append("P: ").append(pregunta).append("\nR: ").append(respuesta).append("\n\n")
            );
        }
        
        if (mensaje.length() == 0) {
            mensaje.append("Postulación sin mensaje adicional");
        }
        
        return new CreatePostulacionCommand(
                proyectoId,
                ilustradorId,
                mensaje.toString().trim(),
                "EN_ESPERA",
                resource.fecha()
        );
    }
}
