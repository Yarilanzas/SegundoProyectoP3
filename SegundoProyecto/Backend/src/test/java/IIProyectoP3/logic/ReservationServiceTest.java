package IIProyectoP3.logic;

import domain.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private final ReservationService service = new ReservationService();

    private Reservation reservaValida() {
        Reservation r = new Reservation();
        r.setActivity("Reunión");
        r.setDate(LocalDate.now().plusDays(1));
        r.setStartTime("08:00");
        r.setEndTime("09:00");
        r.setEmployee(new Employee());
        return r;
    }

    @Test
    void rechazaHoraFinAntesDeInicio() {
        Reservation r = reservaValida();
        r.setStartTime("10:00");
        r.setEndTime("09:00");
        Exception ex = assertThrows(Exception.class,
                () -> service.save(r, List.of(new Category())));
        assertEquals("La hora de fin debe ser posterior a la de inicio", ex.getMessage());
    }

    @Test
    void rechazaSinCategorias() {
        Exception ex = assertThrows(Exception.class,
                () -> service.save(reservaValida(), new ArrayList<>()));
        assertEquals("Debe seleccionar al menos una categoría", ex.getMessage());
    }

    @Test
    void rechazaHoraConFormatoInvalido() {
        Reservation r = reservaValida();
        r.setStartTime("ocho");
        Exception ex = assertThrows(Exception.class,
                () -> service.save(r, List.of(new Category())));
        assertEquals("Formato de hora inválido (use HH:mm)", ex.getMessage());
    }

    @Test
    void generaSiguienteId() {
        Reservation r1 = new Reservation();
        r1.setId("RES-001");
        Reservation r2 = new Reservation();
        r2.setId("RES-007");
        assertEquals("RES-008", service.generateNextId(List.of(r1, r2)));
    }

    @Test
    void generaPrimerIdSiNoHayReservas() {
        assertEquals("RES-001", service.generateNextId(new ArrayList<>()));
    }
}