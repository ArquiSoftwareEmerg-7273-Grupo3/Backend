package com.drawnet.artcollab.portafolioservice.interfaces;

import com.drawnet.artcollab.portafolioservice.application.internal.commandservices.PortafolioCommandServiceImpl;
import com.drawnet.artcollab.portafolioservice.application.internal.queryservices.PortafolioQueryServiceImpl;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Categoria;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.portafolioservice.domain.model.queries.ObtenerCategoriaConIlustracionesQuery;
import com.drawnet.artcollab.portafolioservice.domain.model.queries.ObtenerCategoriasPorPortafolioQuery;
import com.drawnet.artcollab.portafolioservice.infrastructure.external.clients.IlustradorCliente;
import com.drawnet.artcollab.portafolioservice.infrastructure.security.JwtService;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.ActualizarCategoriaResource;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.AgregarIlustracionACategoriaResource;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.CrearCategoriaResource;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.transform.ActualizarCategoriaCommandFromResourceAssembler;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.transform.AgregarIlustracionACategoriaCommandFromResourceAssembler;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.transform.CrearCategoriaCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestión de categorías dentro de los portafolios
 */
@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías en portafolios")
public class CategoriaController {
    
    private final PortafolioCommandServiceImpl commandService;
    private final PortafolioQueryServiceImpl queryService;
    private final JwtService jwtService;
    private final IlustradorCliente ilustradorCliente;
    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    public CategoriaController(
            PortafolioCommandServiceImpl commandService,
            PortafolioQueryServiceImpl queryService,
            JwtService jwtService,
            IlustradorCliente ilustradorCliente) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.jwtService = jwtService;
        this.ilustradorCliente = ilustradorCliente;
    }

    /**
     * Crear una nueva categoría en un portafolio
     */
    @PostMapping("/portafolio/{portafolioId}")
    public ResponseEntity<?> crearCategoria(
            @PathVariable Long portafolioId,
            @RequestBody CrearCategoriaResource resource) {
        try {
            var command = CrearCategoriaCommandFromResourceAssembler.toCommandFromResource(resource, portafolioId);
            var result = commandService.handle(command);
            
            if (result.isPresent()) {
                Categoria categoria = result.get();
                return ResponseEntity.ok().body(
                    String.format("Categoría '%s' creada con ID: %d", categoria.getNombre(), categoria.getId())
                );
            }
            return ResponseEntity.badRequest().body("Error al crear la categoría.");
        } catch (RuntimeException e) {
            logger.error("Error al crear categoría: ", e);
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    /**
     * Obtener todas las categorías de un portafolio con sus ilustraciones
     */
    @GetMapping("/portafolio/{portafolioId}")
    public ResponseEntity<List<Categoria>> obtenerCategoriasPorPortafolio(@PathVariable Long portafolioId) {
        var result = queryService.handle(new ObtenerCategoriasPorPortafolioQuery(portafolioId));
        return ResponseEntity.ok(result);
    }

    /**
     * Obtener una categoría específica con sus ilustraciones
     */
    @GetMapping("/{categoriaId}")
    public ResponseEntity<?> obtenerCategoria(@PathVariable Long categoriaId) {
        Optional<Categoria> result = queryService.handle(new ObtenerCategoriaConIlustracionesQuery(categoriaId));
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualizar una categoría
     */
    @PutMapping("/{categoriaId}")
    public ResponseEntity<?> actualizarCategoria(
            @PathVariable Long categoriaId,
            @RequestBody ActualizarCategoriaResource resource) {
        try {
            var command = ActualizarCategoriaCommandFromResourceAssembler.toCommandFromResource(resource);
            Optional<Categoria> result = commandService.actualizarCategoria(categoriaId, command);
            
            return result.map(categoria -> ResponseEntity.ok().body(
                    String.format("Categoría '%s' actualizada exitosamente", categoria.getNombre())
            )).orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            logger.error("Error al actualizar categoría: ", e);
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    /**
     * Eliminar una categoría
     */
    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long categoriaId) {
        try {
            commandService.eliminarCategoria(categoriaId);
            return ResponseEntity.ok("Categoría eliminada exitosamente");
        } catch (RuntimeException e) {
            logger.error("Error al eliminar categoría: ", e);
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    /**
     * Agregar una ilustración a una categoría.
     * El userId se extrae del token JWT y se convierte a ilustradorId.
     */
    @PostMapping("/{categoriaId}/ilustraciones")
    public ResponseEntity<?> agregarIlustracion(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
            @PathVariable Long categoriaId,
            @RequestBody AgregarIlustracionACategoriaResource resource) {
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
            
            var command = AgregarIlustracionACategoriaCommandFromResourceAssembler
                    .toCommandFromResource(resource, categoriaId, ilustradorId);
            var result = commandService.handle(command);
            
            return result.map(ilustracion -> ResponseEntity.ok().body(
                    String.format("Ilustración '%s' agregada con ID: %d", ilustracion.getTitulo(), ilustracion.getId())
            )).orElse(ResponseEntity.badRequest().build());
        } catch (RuntimeException e) {
            logger.error("Error al agregar ilustración: ", e);
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    
}
