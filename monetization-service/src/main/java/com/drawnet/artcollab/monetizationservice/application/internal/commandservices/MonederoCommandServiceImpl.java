package com.drawnet.artcollab.monetizationservice.application.internal.commandservices;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Donacion;
import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Monedero;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.ActualizarMonederoCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearDonacionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearMonederoCommand;
import com.drawnet.artcollab.monetizationservice.domain.services.MonederoCommandService;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.DonacionRepository;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.MonederoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MonederoCommandServiceImpl implements MonederoCommandService {

    public final MonederoRepository monederoRepository;
    public final DonacionRepository donacionRepository;

    public MonederoCommandServiceImpl(MonederoRepository monederoRepository, DonacionRepository donacionRepository) {
        this.monederoRepository = monederoRepository;
        this.donacionRepository = donacionRepository;
    }

    @Override
    public Optional<Monedero> handle(CrearMonederoCommand command) {
        if(monederoRepository.existsByUsuarioId(command.usuarioId())){
            throw new IllegalArgumentException("Ya existe un monedero para el usuario con ID: " + command.usuarioId());
        }
        var monedero = new Monedero(command);
        var createdMonedero = monederoRepository.save(monedero);
        return Optional.of(createdMonedero);
    }

    @Override
    public Optional<Monedero> handle(ActualizarMonederoCommand command) {
        Optional<Monedero> monederoOptional = monederoRepository.findByUsuarioId(command.usuarioId());
        if (monederoOptional.isPresent()) {
            Monedero monedero = monederoOptional.get();
            monedero.comprarMonedas(command.saldo());
            monederoRepository.save(monedero);
            return Optional.of(monedero);
        }
        return Optional.empty();
    }

    @Transactional
    public Donacion donar(Monedero donante, Monedero receptor, CrearDonacionCommand command) {
        donante.restarSaldo(command.monto());
        receptor.sumarSaldo(command.monto());
        monederoRepository.save(donante);
        monederoRepository.save(receptor);
        Donacion donacion = new Donacion(command);
        donacionRepository.save(donacion);
        return donacion;
    }
}