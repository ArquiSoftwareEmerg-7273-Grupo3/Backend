package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest;


import com.drawnet.artcollab.CollaborativeProjects.application.internal.commandservices.ProyectoCommandServiceImpl;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CerrarProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.FinalizarProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetAllProyectosQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.ProyectoQueryService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients.EscritorClient;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CreateProyectoResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.ProyectoResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform.CreateProyectoCommandFromResourceAssembler;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform.ProyectoResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;
import java.util.stream.Collectors;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/proyectos", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Proyecto", description = "Operaciones relacionadas con proyectos")
public class ProyectoController {

    private final ProyectoCommandServiceImpl proyectoCommandService;
    private final ProyectoQueryService proyectoQueryService;
    private final EscritorClient escritorClient;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(ProyectoController.class);


    public ProyectoController(ProyectoCommandServiceImpl proyectoCommandService, ProyectoQueryService proyectoQueryService, EscritorClient escritorClient, JwtService jwtService) {
        this.proyectoCommandService = proyectoCommandService;
        this.proyectoQueryService = proyectoQueryService;
        this.escritorClient = escritorClient;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Crear un proyecto", description = "Crea un proyecto con los datos proporcionados en el cuerpo de la solicitud")
    @PostMapping
    public ResponseEntity<?> crearProyecto(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateProyectoResource resource) {
        try {
            String token = jwtService.cleanToken(authHeader);
            Long userId = jwtService.extractUserId(token);  

            if (!jwtService.isEscritor(token)) {
                return ResponseEntity.status(403).body("El usuario no tiene el rol de ESCRITOR.");
            }

            var escritor = escritorClient.obtenerEscritorPorUserId(userId);
            if (escritor == null) {
                return ResponseEntity.status(404)
                    .body("No se encontró un perfil de escritor para este usuario. Debe crear su perfil primero.");
            }

            Long escritorId = escritor.id();

            var command = CreateProyectoCommandFromResourceAssembler.toCommandFromResource(resource, escritorId);
            var result = proyectoCommandService.handle(command);

            if (result.isPresent()) {
                Long proyectoId = result.get().getId();
                return ResponseEntity.ok("Proyecto creado con éxito. ID: " + proyectoId);
            }
            return ResponseEntity.badRequest().body("Error al crear el proyecto.");
        } catch (Exception e) {
            logger.error("Error interno al procesar la solicitud: ", e);
            return ResponseEntity.status(500).body("Error interno al procesar la solicitud.");
        }
    }

    @Operation(summary = "Obtener proyectos", description = "Obtiene todas los proyectos en la solicitud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyectos encontradas"),
            @ApiResponse(responseCode = "404", description = "Proyectos no encontradas")
    })
    @GetMapping
    public ResponseEntity<List<ProyectoResource>> getAllProyectos() {
        List<Proyecto> proyectos = proyectoQueryService
                .handle(new GetAllProyectosQuery());
        return ResponseEntity.ok(proyectos.stream()
                .map(ProyectoResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Obtener proyecto por ID", description = "Obtiene un proyecto específico por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProyectoById(@PathVariable Long id) {
        try {
            var proyecto = proyectoQueryService.getById(id);
            if (proyecto.isPresent()) {
                return ResponseEntity.ok(ProyectoResourceFromEntityAssembler.toResourceFromEntity(proyecto.get()));
            }
            return ResponseEntity.status(404).body("Proyecto no encontrado");
        } catch (Exception e) {
            logger.error("Error al obtener proyecto: ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener proyectos por escritor", description = "Obtiene todas los proyectos por escritor en la solicitud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyectos encontradas"),
            @ApiResponse(responseCode = "404", description = "Proyectos no encontradas")
    })
    @GetMapping("/escritorId/{escritorId}")
    public ResponseEntity<List<ProyectoResource>> getProyectosByEscritorId(@PathVariable Long escritorId) {
        List<Proyecto> proyectos = proyectoQueryService.getByEscritorId (escritorId);
        return ResponseEntity.ok(proyectos.stream()
                .map(ProyectoResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Obtener mis proyectos", description = "El escritor obtiene sus proyectos")
    @GetMapping("/mis-proyectos")
    public ResponseEntity<?> getMisProyectos(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtService.cleanToken(authHeader);
            Long userId = jwtService.extractUserId(token);
            
            if (!jwtService.isEscritor(token)) {
                return ResponseEntity.status(403).body("Solo escritores pueden ver sus proyectos");
            }
            
            var escritor = escritorClient.obtenerEscritorPorUserId(userId);
            if (escritor == null) {
                return ResponseEntity.status(404).body("Perfil de escritor no encontrado");
            }
            
            List<Proyecto> proyectos = proyectoQueryService.getByEscritorId(escritor.id());
            return ResponseEntity.ok(proyectos.stream()
                    .map(ProyectoResourceFromEntityAssembler::toResourceFromEntity)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            logger.error("Error al obtener proyectos: ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Operation(summary = "Cerrar proyecto", description = "El escritor cierra un proyecto")
    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarProyecto(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtService.cleanToken(authHeader);
            Long userId = jwtService.extractUserId(token);
            
            if (!jwtService.isEscritor(token)) {
                return ResponseEntity.status(403).body("Solo escritores pueden cerrar proyectos");
            }
            
            var escritor = escritorClient.obtenerEscritorPorUserId(userId);
            if (escritor == null) {
                return ResponseEntity.status(404).body("Perfil de escritor no encontrado");
            }
            
            var command = new CerrarProyectoCommand(id, escritor.id());
            var result = proyectoCommandService.handle(command);
            
            if (result.isPresent()) {
                return ResponseEntity.ok("Proyecto cerrado exitosamente");
            }
            return ResponseEntity.badRequest().body("Error al cerrar el proyecto");
        } catch (Exception e) {
            logger.error("Error al cerrar proyecto: ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Operation(summary = "Finalizar proyecto", description = "El escritor finaliza un proyecto")
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarProyecto(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtService.cleanToken(authHeader);
            Long userId = jwtService.extractUserId(token);
            
            if (!jwtService.isEscritor(token)) {
                return ResponseEntity.status(403).body("Solo escritores pueden finalizar proyectos");
            }
            
            var escritor = escritorClient.obtenerEscritorPorUserId(userId);
            if (escritor == null) {
                return ResponseEntity.status(404).body("Perfil de escritor no encontrado");
            }
            
            var command = new FinalizarProyectoCommand(id, escritor.id());
            var result = proyectoCommandService.handle(command);
            
            if (result.isPresent()) {
                return ResponseEntity.ok("Proyecto finalizado exitosamente");
            }
            return ResponseEntity.badRequest().body("Error al finalizar el proyecto");
        } catch (Exception e) {
            logger.error("Error al finalizar proyecto: ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}