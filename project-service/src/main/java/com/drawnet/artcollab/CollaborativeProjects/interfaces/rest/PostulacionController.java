package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.AprobarPostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CancelarPostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.RechazarPostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetAllPostulacionesQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.PostulacionCommandService;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.PostulacionQueryService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients.EscritorClient;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients.IlustradorClient;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients.UsuarioCliente;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.security.JwtService;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.AprobarPostulacionResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CreatePostulacionResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.PostulacionResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.RechazarPostulacionResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.UserResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform.CreatePostulacionCommandFromResourceAssembler;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform.PostulacionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/postulaciones", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Postulación", description = "Operaciones relacionadas con postulaciones")
public class PostulacionController {

    private final PostulacionCommandService postulacionCommandService;
    private final PostulacionQueryService postulacionQueryService;
    private final UsuarioCliente usuarioCliente;
    private final JwtService jwtService;
    private final EscritorClient escritorClient;
    private final IlustradorClient ilustradorClient;
    private static final Logger logger = LoggerFactory.getLogger(PostulacionController.class);

    public PostulacionController(PostulacionCommandService postulacionCommandService, 
                                PostulacionQueryService postulacionQueryService, 
                                UsuarioCliente usuarioCliente,
                                JwtService jwtService,
                                EscritorClient escritorClient,
                                IlustradorClient ilustradorClient) {
        this.postulacionCommandService = postulacionCommandService;
        this.postulacionQueryService = postulacionQueryService;
        this.usuarioCliente = usuarioCliente;
        this.jwtService = jwtService;
        this.escritorClient = escritorClient;
        this.ilustradorClient = ilustradorClient;
    }

    @Operation(summary = "Crear una postulación", description = "Crea una postulación con los datos proporcionados en el cuerpo de la solicitud")
    @ApiResponse(responseCode = "201", description = "Postulación creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    //@PostMapping
    //public ResponseEntity<PostulacionResource> createPostulacion(@RequestBody CreatePostulacionResource resource) {
    //    Optional<Postulacion> postulacion = postulacionCommandService
    //            .handle(CreatePostulacionCommandFromResourceAssembler.toCommandFromResource(resource));
    //    return postulacion.map(source -> new ResponseEntity<>(PostulacionResourceFromEntityAssembler.toResourceFromEntity(source), CREATED))
    //            .orElseGet(() -> ResponseEntity.badRequest().build());
    //}
    //@PostMapping("/proyecto/{proyectoId}")
    //public ResponseEntity<PostulacionResource> createPostulacion(
    //        @PathVariable Long proyectoId,
    //        @RequestBody CreatePostulacionResource resource,
    //        @RequestParam Long ilustradorId) { // Recibe ilustradorId directamente
//
    //    var command = new CreatePostulacionCommand(
    //            proyectoId,
    //            ilustradorId,
    //            "EN ESPERA",
    //            resource.fecha()
    //    );
    //    Optional<Postulacion> postulacion = postulacionCommandService.handle(command);
//
    //    return postulacion.map(source -> new ResponseEntity<>(
    //                    PostulacionResourceFromEntityAssembler.toResourceFromEntity(source), CREATED))
    //            .orElseGet(() -> ResponseEntity.badRequest().build());
    //}
    @PostMapping("/proyecto/{proyectoId}/ilustrador/{ilustradorId}")
    public ResponseEntity<?> crearPostulacion(
            @PathVariable Long proyectoId,
            @PathVariable Long ilustradorId,
            @RequestBody CreatePostulacionResource resource) {
        try {
            UserResource user = usuarioCliente.verificarUsuario(ilustradorId);
            if (user == null) {
                return ResponseEntity.status(404).body("Usuario no encontrado.");
            }

            if (!"ILUSTRADOR".equals(user.role())) {
                return ResponseEntity.status(403).body("El usuario no tiene el rol de ILUSTRADOR.");
            }

            var command = CreatePostulacionCommandFromResourceAssembler.toCommandFromResource(proyectoId, ilustradorId, resource);
            var result = postulacionCommandService.handle(command);

            if (result.isPresent()) {
                Long postulacionId = result.get().getId();
                return ResponseEntity.ok().body("Postulación creada con ID: " + postulacionId);
            }
            return ResponseEntity.badRequest().body("Error al crear la postulación.");
        } catch (Exception e) {
            logger.error("Error interno al procesar la solicitud: ", e);
            return ResponseEntity.status(500).body("Error interno al procesar la solicitud.");
        }
    }

    @Operation(summary = "Obtener postulaciones", description = "Obtiene todas las postulaciones en la solicitud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Postulaciones encontradas"),
            @ApiResponse(responseCode = "404", description = "Postulaciones no encontradas")
    })
    @GetMapping
    public ResponseEntity<List<PostulacionResource>> getAllPostulaciones() {
        List<Postulacion> postulaciones = postulacionQueryService
                .handle(new GetAllPostulacionesQuery());
        return ResponseEntity.ok(postulaciones.stream()
                .map(PostulacionResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Obtener postulaciones por ilustrador", description = "Obtiene todas las postulaciones por ilustrador en la solicitud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Postulaciones encontradas"),
            @ApiResponse(responseCode = "404", description = "Postulaciones no encontradas")
    })
    @GetMapping("/ilustradorId/{ilustradorId}")
    public ResponseEntity<List<PostulacionResource>> getPostulacionesByIlustradorId(@PathVariable Long ilustradorId) {
        List<Postulacion> postulaciones = postulacionQueryService.getByIlustradorId(ilustradorId);
        return ResponseEntity.ok(postulaciones.stream()
                .map(PostulacionResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Obtener postulaciones por proyecto", description = "Obtiene todas las postulaciones por proyecto en la solicitud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Postulaciones encontradas"),
            @ApiResponse(responseCode = "404", description = "Postulaciones no encontradas")
    })
    @GetMapping("/proyectoId/{proyectoId}")
    public ResponseEntity<List<PostulacionResource>> getPostulacionesByProyectoId(@PathVariable Long proyectoId) {
        List<Postulacion> postulaciones = postulacionQueryService.getByProyectoId(proyectoId);
        return ResponseEntity.ok(postulaciones.stream()
                .map(PostulacionResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Aprobar postulación", description = "El escritor aprueba una postulación")
    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<?> aprobarPostulacion(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
            @RequestBody AprobarPostulacionResource resource) {
        try {
            String token = jwtService.cleanToken(authHeader);
            Long userId = jwtService.extractUserId(token);
            
            if (!jwtService.isEscritor(token)) {
                return ResponseEntity.status(403).body("Solo escritores pueden aprobar postulaciones");
            }
            
            var escritor = escritorClient.obtenerEscritorPorUserId(userId);
            if (escritor == null) {
                return ResponseEntity.status(404).body("Perfil de escritor no encontrado");
            }
            
            var command = new AprobarPostulacionCommand(id, escritor.id(), resource.respuesta());
            var result = postulacionCommandService.handle(command);
            
            if (result.isPresent()) {
                return ResponseEntity.ok("Postulación aprobada exitosamente");
            }
            return ResponseEntity.badRequest().body("Error al aprobar la postulación");
        } catch (Exception e) {
            logger.error("Error al aprobar postulación: ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Operation(summary = "Rechazar postulación", description = "El escritor rechaza una postulación")
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarPostulacion(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
            @RequestBody RechazarPostulacionResource resource) {
        try {
            String token = jwtService.cleanToken(authHeader);
            Long userId = jwtService.extractUserId(token);
            
            if (!jwtService.isEscritor(token)) {
                return ResponseEntity.status(403).body("Solo escritores pueden rechazar postulaciones");
            }
            
            var escritor = escritorClient.obtenerEscritorPorUserId(userId);
            if (escritor == null) {
                return ResponseEntity.status(404).body("Perfil de escritor no encontrado");
            }
            
            var command = new RechazarPostulacionCommand(id, escritor.id(), resource.razon());
            var result = postulacionCommandService.handle(command);
            
            if (result.isPresent()) {
                return ResponseEntity.ok("Postulación rechazada");
            }
            return ResponseEntity.badRequest().body("Error al rechazar la postulación");
        } catch (Exception e) {
            logger.error("Error al rechazar postulación: ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Operation(summary = "Cancelar postulación", description = "El ilustrador cancela su postulación")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPostulacion(
            @PathVariable Long id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtService.cleanToken(authHeader);
            Long userId = jwtService.extractUserId(token);
            
            if (!jwtService.isIlustrador(token)) {
                return ResponseEntity.status(403).body("Solo ilustradores pueden cancelar sus postulaciones");
            }
            
            var ilustrador = ilustradorClient.obtenerIlustradorPorUserId(userId);
            if (ilustrador == null) {
                return ResponseEntity.status(404).body("Perfil de ilustrador no encontrado");
            }
            
            var command = new CancelarPostulacionCommand(id, ilustrador.id());
            var result = postulacionCommandService.handle(command);
            
            if (result.isPresent()) {
                return ResponseEntity.ok("Postulación cancelada");
            }
            return ResponseEntity.badRequest().body("Error al cancelar la postulación");
        } catch (Exception e) {
            logger.error("Error al cancelar postulación: ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener mis postulaciones", description = "El ilustrador obtiene sus postulaciones")
    @GetMapping("/mis-postulaciones")
    public ResponseEntity<?> getMisPostulaciones(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtService.cleanToken(authHeader);
            Long userId = jwtService.extractUserId(token);
            
            if (!jwtService.isIlustrador(token)) {
                return ResponseEntity.status(403).body("Solo ilustradores pueden ver sus postulaciones");
            }
            
            var ilustrador = ilustradorClient.obtenerIlustradorPorUserId(userId);
            if (ilustrador == null) {
                return ResponseEntity.status(404).body("Perfil de ilustrador no encontrado");
            }
            
            List<Postulacion> postulaciones = postulacionQueryService.getByIlustradorId(ilustrador.id());
            return ResponseEntity.ok(postulaciones.stream()
                    .map(PostulacionResourceFromEntityAssembler::toResourceFromEntity)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            logger.error("Error al obtener postulaciones: ", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}