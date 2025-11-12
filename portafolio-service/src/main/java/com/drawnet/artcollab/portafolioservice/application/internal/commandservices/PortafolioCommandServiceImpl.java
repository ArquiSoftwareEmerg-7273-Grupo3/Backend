package com.drawnet.artcollab.portafolioservice.application.internal.commandservices;

import com.drawnet.artcollab.portafolioservice.domain.model.aggregates.Portafolio;
import com.drawnet.artcollab.portafolioservice.domain.model.commands.*;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Categoria;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.portafolioservice.domain.services.PortafolioCommandService;
import com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories.CategoriaRepository;
import com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories.IlustracionRepository;
import com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories.PortafolioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PortafolioCommandServiceImpl implements PortafolioCommandService {
    private final PortafolioRepository portafolioRepository;
    private final CategoriaRepository categoriaRepository;
    private final IlustracionRepository ilustracionRepository;

    public PortafolioCommandServiceImpl(
            PortafolioRepository portafolioRepository,
            CategoriaRepository categoriaRepository,
            IlustracionRepository ilustracionRepository) {
        this.portafolioRepository = portafolioRepository;
        this.categoriaRepository = categoriaRepository;
        this.ilustracionRepository = ilustracionRepository;
    }

    //@Override
    //public Optional<Portafolio> handle(CrearPortafolioCommand command) {
    //    Portafolio portafolio = new Portafolio(command.ilustradorId(), command.titulo(),command.descripcion(),command.urlImagen());
    //    return Optional.of(portafolioRepository.save(portafolio).getId());
    //}

    @Override
    public Optional<Portafolio> handle(CrearPortafolioCommand command) {
        Portafolio portafolio = new Portafolio(command.ilustradorId(),command.titulo(), command.descripcion(), command.urlImagen());
        return Optional.of(portafolioRepository.save(portafolio));


        //Long nuevoId = portafolioRepository.findMaxIdByIlustradorId(command.ilustradorId()) + 1;
        //Portafolio portafolio = new Portafolio(command.ilustradorId(), command.titulo(), command.descripcion(), command.urlImagen());
        //portafolio.setId(nuevoId); // Asignar el nuevo ID personalizado
        //return Optional.of(portafolioRepository.save(portafolio));

    }

    /**
     * @deprecated Este método está obsoleto. Use handle(AgregarIlustracionACategoriaCommand) en su lugar.
     * Las ilustraciones ahora deben agregarse a categorías, no directamente a portafolios.
     */
    @Override
    @Deprecated
    public Optional<Ilustracion> handle(AgregarIlustracionAPortafolioCommand command) {
        throw new UnsupportedOperationException(
                "Este método está obsoleto. Las ilustraciones deben agregarse a categorías usando AgregarIlustracionACategoriaCommand"
        );
    }

    @Override
    public Optional<Ilustracion> handle(PublicarIlustracionCommand command) {
        // Nueva implementación: al publicar una ilustración ahora se agrega a una categoría.
        // 1) Buscar el portafolio del ilustrador
        var portafolios = portafolioRepository.findByIlustradorId(command.ilustradorId());
        if (portafolios == null || portafolios.isEmpty()) {
            throw new RuntimeException("No se encontró ningún portafolio para el ilustrador con id: " + command.ilustradorId());
        }

        var portafolio = portafolios.get(0);

        // 2) Intentar obtener la primera categoría (orden asc)
        var categorias = categoriaRepository.findByPortafolioIdOrderByOrdenAsc(portafolio.getId());
        Categoria categoria;

        if (categorias == null || categorias.isEmpty()) {
            // Si no hay categorías, crear una categoría por defecto "General"
            categoria = new Categoria("General", "Categoría por defecto", 0, portafolio);
            categoria = categoriaRepository.save(categoria);
            portafolio.agregarCategoria(categoria);
        } else {
            categoria = categorias.get(0);
        }

        // 3) Delegar a AgregarIlustracionACategoriaCommand para respetar la nueva estructura
        var agregarCmd = new AgregarIlustracionACategoriaCommand(
                categoria.getId(),
                command.ilustradorId(),
                null,
                command.titulo(),
                command.descripcion(),
                command.urlImagen(),
                command.publicada()
        );

        return handle(agregarCmd);
    }

    //@Override
    //public void handle(CalificarIlustracionCommand command) {
    //    ilustracionRepository.findById(command.ilustracionId()).ifPresent(ilustracion -> {
    //        ilustracion.agregarCalificacion(command.usuarioId(), command.puntuacion(), command.comentario());
    //        ilustracionRepository.save(ilustracion);
    //    });
    //}

    @Override
    public void handle(CalificarIlustracionCommand command) {
        ilustracionRepository.findById(command.ilustracionId()).ifPresent(ilustracion -> {
            ilustracion.agregarCalificacion(command.usuarioId(), command.puntuacion(), command.comentario());
            System.out.println("Calificación agregada: " + command.puntuacion() + " - " + command.comentario());
            ilustracionRepository.save(ilustracion);
        });
    }

    @Override
    public void eliminarPortafolio(Long portafolioId) {
        portafolioRepository.deleteById(portafolioId);
    }

    @Override
    public Optional<Portafolio> actualizarPortafolio(Long portafolioId, ActualizarPortafolioCommand command) {
        Optional<Portafolio> portafolioOpt = portafolioRepository.findById(portafolioId);
        if (portafolioOpt.isEmpty()) return Optional.empty();

        Portafolio portafolio = portafolioOpt.get();
        portafolio.setTitulo(command.titulo());
        portafolio.setDescripcion(command.descripcion());
        portafolio.setUrlImagen(command.urlImagen());

        return Optional.of(portafolioRepository.save(portafolio));
    }

    @Override
    public void eliminarIlustracion(Long ilustracionId) {
        ilustracionRepository.deleteById(ilustracionId);
    }

    @Override
    public Optional<Ilustracion> actualizarIlustracion(Long ilustracionId, ActualizarIlustracionCommand command) {
        Optional<Ilustracion> ilustracionOpt = ilustracionRepository.findById(ilustracionId);
        if (ilustracionOpt.isEmpty()) return Optional.empty();

        Ilustracion ilustracion = ilustracionOpt.get();
        ilustracion.setTitulo(command.titulo());
        ilustracion.setDescripcion(command.descripcion());
        ilustracion.setUrlImagen(command.urlImagen());

        return Optional.of(ilustracionRepository.save(ilustracion));
    }

    // ============================================
    // COMANDOS DE CATEGORÍA
    // ============================================
    
    @Override
    @Transactional
    public Optional<Categoria> handle(CrearCategoriaCommand command) {
        // Buscar el portafolio
        Optional<Portafolio> portafolioOpt = portafolioRepository.findById(command.portafolioId());
        if (portafolioOpt.isEmpty()) {
            throw new RuntimeException("Portafolio no encontrado con ID: " + command.portafolioId());
        }
        
        Portafolio portafolio = portafolioOpt.get();
        
        // Verificar si ya existe una categoría con ese nombre
        if (categoriaRepository.existsByPortafolioIdAndNombre(command.portafolioId(), command.nombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + command.nombre());
        }
        
        // Crear la categoría
        Integer orden = command.orden() != null ? command.orden() : 0;
        Categoria categoria = new Categoria(command.nombre(), command.descripcion(), orden, portafolio);
        
        // Guardar
        Categoria savedCategoria = categoriaRepository.save(categoria);
        portafolio.agregarCategoria(savedCategoria);
        
        return Optional.of(savedCategoria);
    }
    
    @Override
    @Transactional
    public void eliminarCategoria(Long categoriaId) {
        categoriaRepository.deleteById(categoriaId);
    }
    
    @Override
    @Transactional
    public Optional<Categoria> actualizarCategoria(Long categoriaId, ActualizarCategoriaCommand command) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(categoriaId);
        if (categoriaOpt.isEmpty()) return Optional.empty();
        
        Categoria categoria = categoriaOpt.get();
        
        if (command.nombre() != null) {
            categoria.setNombre(command.nombre());
        }
        if (command.descripcion() != null) {
            categoria.setDescripcion(command.descripcion());
        }
        if (command.orden() != null) {
            categoria.setOrden(command.orden());
        }
        
        return Optional.of(categoriaRepository.save(categoria));
    }
    
    @Override
    @Transactional
    public Optional<Ilustracion> handle(AgregarIlustracionACategoriaCommand command) {
        // Buscar la categoría
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(command.categoriaId());
        if (categoriaOpt.isEmpty()) {
            throw new RuntimeException("Categoría no encontrada con ID: " + command.categoriaId());
        }
        
        Categoria categoria = categoriaOpt.get();
        Ilustracion ilustracion;
        
        if (command.ilustracionId() == null || command.ilustracionId() == 0) {
            // Crear nueva ilustración
            ilustracion = new Ilustracion(
                    command.ilustradorId(),
                    command.titulo(),
                    command.descripcion(),
                    command.urlImagen(),
                    categoria
            );
            
            if (command.publicada() != null && command.publicada()) {
                ilustracion.publicar();
            }
            
            ilustracion = ilustracionRepository.save(ilustracion);
        } else {
            // Buscar ilustración existente y moverla a esta categoría
            ilustracion = ilustracionRepository.findById(command.ilustracionId())
                    .orElseThrow(() -> new RuntimeException("Ilustración no encontrada con ID: " + command.ilustracionId()));
            
            ilustracion.setCategoria(categoria);
            ilustracion = ilustracionRepository.save(ilustracion);
        }
        
        categoria.agregarIlustracion(ilustracion);
        return Optional.of(ilustracion);
    }

}
