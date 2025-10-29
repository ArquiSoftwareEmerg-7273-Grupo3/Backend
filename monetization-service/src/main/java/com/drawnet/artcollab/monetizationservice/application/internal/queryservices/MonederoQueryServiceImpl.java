package com.drawnet.artcollab.monetizationservice.application.internal.queryservices;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Monedero;
import com.drawnet.artcollab.monetizationservice.domain.services.MonederoQueryService;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.MonederoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MonederoQueryServiceImpl implements MonederoQueryService {

    private  final MonederoRepository monederoRepository;

    public MonederoQueryServiceImpl(MonederoRepository monederoRepository) {
        this.monederoRepository = monederoRepository;
    }

    @Override
    public Optional<Monedero> getByUsuarioId(Long usuarioId) {
        return monederoRepository.findByUsuarioId(usuarioId);
    }
}