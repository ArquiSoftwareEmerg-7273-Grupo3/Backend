package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreatePostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetAllPostulacionesQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.PostulacionCommandService;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.PostulacionQueryService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients.UsuarioCliente;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CreatePostulacionResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.PostulacionResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.UserResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform.CreatePostulacionCommandFromResourceAssembler;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform.PostulacionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/postulaciones", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Postulación", description = "Operaciones relacionadas con postulaciones")
public class PostulacionController {

    private final PostulacionCommandService postulacionCommandService;
    private final PostulacionQueryService postulacionQueryService;
    private final UsuarioCliente usuarioCliente;
    private static final Logger logger = LoggerFactory.getLogger(ProyectoController.class);

    public PostulacionController(PostulacionCommandService postulacionCommandService, PostulacionQueryService postulacionQueryService, UsuarioCliente usuarioCliente) {
        this.postulacionCommandService = postulacionCommandService;
        this.postulacionQueryService = postulacionQueryService;
        this.usuarioCliente = usuarioCliente;
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

    // src/main/java/com/drawnet/artcollab/CollaborativeProjects/interfaces/rest/PostulacionController.java
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoPostulacion(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        try {
            var result = postulacionCommandService.actualizarEstado(id, nuevoEstado);

            if (result.isPresent()) {
                return ResponseEntity.ok("Estado actualizado a: " + nuevoEstado);
            }
            return ResponseEntity.badRequest().body("Error al actualizar el estado.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error interno al procesar la solicitud: ", e);
            return ResponseEntity.status(500).body("Error interno al procesar la solicitud.");
        }
    }

}