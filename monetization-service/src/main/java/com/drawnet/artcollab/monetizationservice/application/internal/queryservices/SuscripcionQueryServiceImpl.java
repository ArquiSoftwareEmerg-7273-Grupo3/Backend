package com.drawnet.artcollab.monetizationservice.application.internal.queryservices;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Suscripcion;
import com.drawnet.artcollab.monetizationservice.domain.services.SuscripcionQueryService;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.SuscripcionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SuscripcionQueryServiceImpl implements SuscripcionQueryService {

    private final SuscripcionRepository suscripcionRepository;

    public SuscripcionQueryServiceImpl(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    @Override
    public Optional<Suscripcion> getByUsuarioId(Long usuarioId) {
        return suscripcionRepository.findByUsuarioId(usuarioId);
    }
}
