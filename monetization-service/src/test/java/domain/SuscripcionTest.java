package domain;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Suscripcion;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.valueobjects.Plan;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SuscripcionTest {

    @Test
    public void testCrearSuscripcionPremium() {
        CrearSuscripcionCommand command = new CrearSuscripcionCommand(
                1L
        );

        Plan plan = Plan.valueOf("PREMIUM");
        Suscripcion suscripcion = new Suscripcion(command, plan);

        assertEquals(1L, suscripcion.getUsuarioId());
        assertEquals(Plan.PREMIUM, suscripcion.getPlan());
        assertEquals(LocalDate.now(), suscripcion.getFechaInicio());
        assertEquals(LocalDate.now().plusMonths(3), suscripcion.getFechaFin());
    }

}
