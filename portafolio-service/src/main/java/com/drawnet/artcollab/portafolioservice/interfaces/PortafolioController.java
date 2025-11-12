package com.drawnet.artcollab.portafolioservice.interfaces;

import com.drawnet.artcollab.portafolioservice.application.internal.commandservices.PortafolioCommandServiceImpl;
import com.drawnet.artcollab.portafolioservice.application.internal.queryservices.PortafolioQueryServiceImpl;
import com.drawnet.artcollab.portafolioservice.domain.model.aggregates.Portafolio;
import com.drawnet.artcollab.portafolioservice.domain.model.commands.ActualizarPortafolioCommand;
import com.drawnet.artcollab.portafolioservice.domain.model.queries.ObtenerPortafoliosPorIlustradorQuery;
import com.drawnet.artcollab.portafolioservice.infrastructure.external.clients.IlustradorCliente;
import com.drawnet.artcollab.portafolioservice.infrastructure.security.JwtService;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.AgregarIlustracionAPortafolioResource;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.CrearPortafolioResource;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.UserResource;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.transform.AgregarIlustracionAPortafolioCommandFromResourceAssembler;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.transform.CrearPortafolioCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/portafolios")
@Tag(name = "Portafolios", description = "Endpoints para gestión de portafolios de ilustradores")
public class PortafolioController {
    private final PortafolioCommandServiceImpl commandService;
    private final PortafolioQueryServiceImpl queryService;
    private final IlustradorCliente ilustradorCliente;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(PortafolioController.class);


    public PortafolioController(PortafolioCommandServiceImpl commandService, 
                                PortafolioQueryServiceImpl queryService, 
                                IlustradorCliente ilustradorCliente,
                                JwtService jwtService) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.ilustradorCliente = ilustradorCliente;
        this.jwtService = jwtService;
    }

   
    @PostMapping
    public ResponseEntity<?> crearPortafolio(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
            @RequestBody CrearPortafolioResource resource) {
        try {
            String token = jwtService.cleanToken(authHeader);
            Long userId = jwtService.extractUserId(token);
            
            if (!jwtService.isIlustrador(token)) {
                return ResponseEntity.status(403).body("El usuario no tiene el rol de ILUSTRADOR.");
            }

            var ilustrador = ilustradorCliente.obtenerIlustradorPorUserId(userId);
            if (ilustrador == null) {
                return ResponseEntity.status(404)
                    .body("No se encontró un perfil de ilustrador para este usuario. Debe crear su perfil primero.");
            }
            
            Long ilustradorId = ilustrador.id();

            var command = CrearPortafolioCommandFromResourceAssembler.toCommandFromResource(resource, ilustradorId);
            var result = commandService.handle(command);

            if (result.isPresent()) {
                Long portafolioId = result.get().getId();
                return ResponseEntity.ok().body("Portafolio creado con ID: " + portafolioId);
            }
            return ResponseEntity.badRequest().body("Error al crear el portafolio.");
        } catch (Exception e) {
            logger.error("Error interno al procesar la solicitud: ", e);
            return ResponseEntity.status(500).body("Error interno al procesar la solicitud: " + e.getMessage());
        }
    }

   
    @PutMapping("/{portafolioId}")
    public ResponseEntity<Portafolio> actualizarPortafolio(
            @PathVariable Long portafolioId,
            @RequestBody ActualizarPortafolioCommand command) {
        Optional<Portafolio> portafolioOpt = commandService.actualizarPortafolio(portafolioId, command);
        return portafolioOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/{portafolioId}/ilustraciones")
    public ResponseEntity<?> agregarIlustracion(
            @PathVariable Long portafolioId,
            @RequestParam Long ilustradorId,
            @RequestBody AgregarIlustracionAPortafolioResource resource) {
        var command = AgregarIlustracionAPortafolioCommandFromResourceAssembler.toCommandFromResource(resource, portafolioId, ilustradorId);
        var result = commandService.handle(command);
        return result.map(ilustracion -> ResponseEntity.ok().body("Ilustración agregada con ID: " + ilustracion.getId()))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/ilustrador/{ilustradorId}")
    public ResponseEntity<?> obtenerPortafoliosPorIlustrador(@PathVariable Long ilustradorId) {
        var result = queryService.handle(new ObtenerPortafoliosPorIlustradorQuery(ilustradorId));
        return ResponseEntity.ok(result);
    }

  
    @GetMapping("/mi-portafolio")
    public ResponseEntity<?> obtenerMiPortafolio(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtService.cleanToken(authHeader);
            Long userId = jwtService.extractUserId(token);
            
            if (!jwtService.isIlustrador(token)) {
                return ResponseEntity.status(403).body("El usuario no tiene el rol de ILUSTRADOR.");
            }
            
            var ilustrador = ilustradorCliente.obtenerIlustradorPorUserId(userId);
            if (ilustrador == null) {
                return ResponseEntity.status(404)
                    .body("No se encontró un perfil de ilustrador para este usuario.");
            }
            
            Long ilustradorId = ilustrador.id();
            
            var result = queryService.handle(new ObtenerPortafoliosPorIlustradorQuery(ilustradorId));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error al obtener portafolio: ", e);
            return ResponseEntity.status(500).body("Error interno al procesar la solicitud: " + e.getMessage());
        }
    }


}
