package com.drawnet.artcollab.monetizationservice.application.internal.queryservices;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Donacion;
import com.drawnet.artcollab.monetizationservice.domain.services.DonacionQueryService;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.DonacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonacionQueryServiceImpl implements DonacionQueryService {

    private final DonacionRepository donacionRepository;

    public DonacionQueryServiceImpl(DonacionRepository donacionRepository) {
        this.donacionRepository = donacionRepository;
    }

    @Override
    public Optional<Donacion> getById(Long id) {
        return donacionRepository.findById(id);
    }

    @Override
    public List<Donacion> getByDonanteId(Long donanteId) {
        return donacionRepository.findByDonanteId(donanteId);
    }

    @Override
    public List<Donacion> getByReceptorId(Long receptorId) {
        return donacionRepository.findByReceptorId(receptorId);
    }
}
