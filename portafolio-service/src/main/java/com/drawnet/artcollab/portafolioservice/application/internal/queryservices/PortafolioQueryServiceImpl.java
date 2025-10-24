package com.drawnet.artcollab.portafolioservice.application.internal.queryservices;

import com.drawnet.artcollab.portafolioservice.domain.model.aggregates.Portafolio;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.portafolioservice.domain.model.queries.*;
import com.drawnet.artcollab.portafolioservice.domain.model.valueobjects.Calificacion;
import com.drawnet.artcollab.portafolioservice.domain.services.PortafolioQueryService;
import com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories.IlustracionRepository;
import com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories.PortafolioRepository;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.IlustracionResumenResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortafolioQueryServiceImpl implements PortafolioQueryService {

    private final PortafolioRepository portafolioRepository;
    private final IlustracionRepository ilustracionRepository;

    public PortafolioQueryServiceImpl(PortafolioRepository portafolioRepository, IlustracionRepository ilustracionRepository) {
        this.portafolioRepository = portafolioRepository;
        this.ilustracionRepository = ilustracionRepository;
    }

    @Override
    public List<Portafolio> handle(ObtenerPortafoliosPorIlustradorQuery query) {
        return portafolioRepository.findByIlustradorId(query.ilustradorId());
    }

    @Override
    public List<Ilustracion> handle(ObtenerIlustracionesPorPortafolioQuery query) {
        return ilustracionRepository.findByPortafolios_Id(query.portafolioId());
    }

    @Override
    public List<Ilustracion> handle(ObtenerIlustracionesPublicadasPorIlustradorQuery query) {
        return ilustracionRepository.findIlustracionesPublicadasByIlustrador(query.ilustradorId());
    }

    //@Override
    //public List<Calificacion> handle(ObtenerCalificacionesDeIlustracionQuery query) {
    //    Optional<Ilustracion> ilustracionOpt = ilustracionRepository.findById(query.ilustracionId());
    //    return ilustracionOpt.map(Ilustracion::getCalificaciones).orElse(List.of());
    //}

    @Override
    public List<Calificacion> handle(ObtenerCalificacionesDeIlustracionQuery query) {
        Optional<Ilustracion> ilustracionOpt = ilustracionRepository.findById(query.ilustracionId());
        if (ilustracionOpt.isPresent()) {
            Ilustracion ilustracion = ilustracionOpt.get();
            // Forzar la inicialización de las calificaciones
            ilustracion.getCalificaciones().size();
            return ilustracion.getCalificaciones();
        }
        return List.of();
    }


    @Override
    public IlustracionResumenResource handle(ObtenerResumenIlustracionQuery query) {
        var ilustracion = ilustracionRepository.findById(query.ilustracionId())
                .orElseThrow(() -> new IllegalArgumentException("Ilustración no encontrada"));

        double promedio = ilustracion.getCalificaciones().stream()
                .mapToInt(Calificacion::getPuntuacion)
                .average()
                .orElse(0.0);

        int cantidad = ilustracion.getCalificaciones().size();

        return new IlustracionResumenResource(
                ilustracion.getTitulo(),
                ilustracion.getDescripcion(),
                promedio,
                cantidad
        );
    }

}
