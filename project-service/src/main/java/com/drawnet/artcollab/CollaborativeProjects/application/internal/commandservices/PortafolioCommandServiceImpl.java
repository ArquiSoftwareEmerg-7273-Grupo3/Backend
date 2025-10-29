package com.drawnet.artcollab.CollaborativeProjects.application.internal.commandservices;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Portafolio;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.*;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.PortafolioCommandService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.IlustracionRepository;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.PortafolioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PortafolioCommandServiceImpl implements PortafolioCommandService {
    private final PortafolioRepository portafolioRepository;
    private final IlustracionRepository ilustracionRepository;

    public PortafolioCommandServiceImpl(PortafolioRepository portafolioRepository, IlustracionRepository ilustracionRepository) {
        this.portafolioRepository = portafolioRepository;
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

    @Override
    public Optional<Ilustracion> handle(AgregarIlustracionAPortafolioCommand command) {
        Optional<Portafolio> portafolioOpt = portafolioRepository.findById(command.portafolioId());
        if (portafolioOpt.isEmpty()) return Optional.empty();

        Portafolio portafolio = portafolioOpt.get();

        Ilustracion ilustracion;
        if (command.ilustracionId() == null || command.ilustracionId() == 0) {
            // Crear nueva ilustración

            //Long nuevoId = ilustracionRepository.findMaxIdByIlustradorId(command.ilustradorId()) + 1;

            ilustracion = new Ilustracion(
                    command.ilustradorId(),
                    command.titulo(),
                    command.descripcion(),
                    command.urlImagen()
            );
            //ilustracion.setId(nuevoId); // Asignar el nuevo ID personalizado
            //
            //ilustracion.setPortafolio(portafolioOpt.get());
            ilustracion = ilustracionRepository.save(ilustracion);
        } else {
            // Buscar ilustración existente
            ilustracion = ilustracionRepository.findById(command.ilustracionId())
                    .orElseThrow(() -> new RuntimeException("Ilustración no encontrada"));
            //
            //ilustracion.setPortafolio(portafolioOpt.get());
        }

        //Ilustracion ilustracion = new Ilustracion(command.ilustradorId(),command.titulo(), command.descripcion(), command.urlImagen());
        //Ilustracion saved = ilustracionRepository.save(ilustracion);

        //Portafolio portafolio = portafolioOpt.get();
        //
        portafolio.agregarIlustracion(ilustracion);
        portafolioRepository.save(portafolio);
        return Optional.of(ilustracion);
    }

    //@Override
    //public Optional<Ilustracion> handle(PublicarIlustracionCommand command) {
    //    Ilustracion ilustracion = new Ilustracion(command.ilustradorId(),command.titulo(), command.descripcion(), command.urlImagen());
    //    Ilustracion saved = ilustracionRepository.save(ilustracion);
    //    return Optional.of(saved);
    //}

    @Override
    public Optional<Ilustracion> handle(PublicarIlustracionCommand command) {
        Ilustracion ilustracion = new Ilustracion(
                command.ilustradorId(),
                command.titulo(),
                command.descripcion(),
                command.urlImagen()
        );
        if (command.publicada()) {
            ilustracion.publicar(); // Marca la ilustración como publicada
        }
        return Optional.of(ilustracionRepository.save(ilustracion));
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

}
