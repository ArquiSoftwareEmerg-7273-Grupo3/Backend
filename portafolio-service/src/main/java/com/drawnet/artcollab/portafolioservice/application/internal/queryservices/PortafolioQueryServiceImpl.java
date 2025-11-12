package com.drawnet.artcollab.portafolioservice.application.internal.queryservices;

import com.drawnet.artcollab.portafolioservice.domain.model.aggregates.Portafolio;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Categoria;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.portafolioservice.domain.model.queries.*;
import com.drawnet.artcollab.portafolioservice.domain.model.valueobjects.Calificacion;
import com.drawnet.artcollab.portafolioservice.domain.services.PortafolioQueryService;
import com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories.CategoriaRepository;
import com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories.IlustracionRepository;
import com.drawnet.artcollab.portafolioservice.infrastructure.persistance.jpa.repositories.PortafolioRepository;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.IlustracionResumenResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortafolioQueryServiceImpl implements PortafolioQueryService {

    private final PortafolioRepository portafolioRepository;
    private final CategoriaRepository categoriaRepository;
    private final IlustracionRepository ilustracionRepository;

    public PortafolioQueryServiceImpl(
            PortafolioRepository portafolioRepository,
            CategoriaRepository categoriaRepository,
            IlustracionRepository ilustracionRepository) {
        this.portafolioRepository = portafolioRepository;
        this.categoriaRepository = categoriaRepository;
        this.ilustracionRepository = ilustracionRepository;
    }

    @Override
    public List<Portafolio> handle(ObtenerPortafoliosPorIlustradorQuery query) {
        return portafolioRepository.findByIlustradorId(query.ilustradorId());
    }

    // ============================================
    // QUERIES DE CATEGORÍA
    // ============================================
    
    @Override
    public List<Categoria> handle(ObtenerCategoriasPorPortafolioQuery query) {
        return categoriaRepository.findByPortafolioIdWithIlustraciones(query.portafolioId());
    }
    
    @Override
    public Optional<Categoria> handle(ObtenerCategoriaConIlustracionesQuery query) {
        return categoriaRepository.findByIdWithIlustraciones(query.categoriaId());
    }
    
    // ============================================
    // QUERIES DE ILUSTRACIÓN
    // ============================================
    
    @Override
    public List<Ilustracion> handle(ObtenerIlustracionesPorCategoriaQuery query) {
        Optional<Categoria> categoria = categoriaRepository.findByIdWithIlustraciones(query.categoriaId());
        return categoria.map(Categoria::getIlustraciones).orElse(List.of());
    }

    /**
     * @deprecated Use ObtenerCategoriasPorPortafolioQuery en su lugar
     */
    @Override
    @Deprecated
    public List<Ilustracion> handle(ObtenerIlustracionesPorPortafolioQuery query) {
        throw new UnsupportedOperationException(
                "Este método está obsoleto. Use ObtenerCategoriasPorPortafolioQuery para obtener categorías con sus ilustraciones"
        );
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
