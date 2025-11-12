package com.drawnet.artcollab.portafolioservice.domain.services;

import com.drawnet.artcollab.portafolioservice.domain.model.aggregates.Portafolio;
import com.drawnet.artcollab.portafolioservice.domain.model.commands.*;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Categoria;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;

import java.util.Optional;

public interface PortafolioCommandService {
    // Comandos de Portafolio
    Optional<Portafolio> handle(CrearPortafolioCommand command);
    void eliminarPortafolio(Long portafolioId);
    Optional<Portafolio> actualizarPortafolio(Long portafolioId, ActualizarPortafolioCommand command);
    
    // Comandos de Categoría
    Optional<Categoria> handle(CrearCategoriaCommand command);
    void eliminarCategoria(Long categoriaId);
    Optional<Categoria> actualizarCategoria(Long categoriaId, ActualizarCategoriaCommand command);
    
    // Comandos de Ilustración
    Optional<Ilustracion> handle(AgregarIlustracionACategoriaCommand command);
    Optional<Ilustracion> handle(PublicarIlustracionCommand command);
    void handle(CalificarIlustracionCommand command);
    void eliminarIlustracion(Long ilustracionId);
    Optional<Ilustracion> actualizarIlustracion(Long ilustracionId, ActualizarIlustracionCommand command);
    
    // DEPRECATED - mantener por compatibilidad temporal
    @Deprecated
    Optional<Ilustracion> handle(AgregarIlustracionAPortafolioCommand command);
}
