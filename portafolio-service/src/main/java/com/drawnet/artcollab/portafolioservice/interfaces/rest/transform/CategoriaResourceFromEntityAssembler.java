package com.drawnet.artcollab.portafolioservice.interfaces.rest.transform;

import com.drawnet.artcollab.portafolioservice.domain.model.entities.Categoria;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.CategoriaResource;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.IlustracionResource;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriaResourceFromEntityAssembler {
    public static CategoriaResource toResource(Categoria categoria) {
        List<IlustracionResource> ilustraciones = categoria.getIlustraciones().stream()
                .map(IlustracionResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());

        return new CategoriaResource(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getOrden(),
                ilustraciones
        );
    }
}
