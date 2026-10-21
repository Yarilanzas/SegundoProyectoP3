package IIProyectoP3.logic;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import  IIProyectoP3.data.CategoryDAO;
import  IIProyectoP3.data.ReservationDAO;
import  IIProyectoP3.data.ResourceDAO;
import  domain.Category;
import  domain.ReservaExtraccion;
import  domain.Reservation;
import  domain.Resource;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {

    private final ReservationDAO reservationDAO;
    private final ResourceDAO resourceDAO;
    private final CategoryDAO categoryDAO;

    public ReservationService(){
        this.reservationDAO = new ReservationDAO();
        this.resourceDAO = new ResourceDAO();
        this.categoryDAO = new CategoryDAO();
    }

    public void save(Reservation newReservation, List<Category> categoriasSeleccionadas) throws Exception {
        if (newReservation.getActivity() == null || newReservation.getActivity().trim().isEmpty()) {
            throw new Exception("La actividad es obligatoria");
        }
        if (newReservation.getEmployee() == null) {
            throw new Exception("La reserva debe tener un funcionario");
        }
        if (newReservation.getDate() == null) {
            throw new Exception("La fecha es obligatoria");
        }
        if (newReservation.getStartTime() == null || newReservation.getStartTime().trim().isEmpty()
                || newReservation.getEndTime() == null || newReservation.getEndTime().trim().isEmpty()) {
            throw new Exception("Las horas de inicio y fin son obligatorias");
        }

        LocalTime inicio, fin;

        try {
            inicio = LocalTime.parse(newReservation.getStartTime());
            fin = LocalTime.parse(newReservation.getEndTime());
        } catch (Exception ex) {
            throw new Exception("Formato de hora inválido (use HH:mm)");
        }
        if (!fin.isAfter(inicio)) {
            throw new Exception("La hora de fin debe ser posterior a la de inicio");
        }
        if (categoriasSeleccionadas == null || categoriasSeleccionadas.isEmpty()) {
            throw new Exception("Debe seleccionar al menos una categoría");
        }

        List<Reservation> todasReservas = reservationDAO.findAll();

        newReservation.setId(generateNextId(todasReservas));
        newReservation.setStatus("ACTIVA");

        List<Resource> recursosAsignados = new ArrayList<>();

        for (Category cat : categoriasSeleccionadas) {
            List<Resource> recursosDeCat = resourceDAO.findByCategory(cat.getId());

            Resource disponible = buscarRecursoDisponible(recursosDeCat, newReservation, todasReservas);

            if (disponible == null) {
                throw new Exception("No hay recursos disponibles para la categoría "
                        + cat.getDescription() + " en el rango de horario seleccionado.");
            }

            recursosAsignados.add(disponible);
        }

        newReservation.setResources(recursosAsignados);
        reservationDAO.create(newReservation);
    }

    private Resource buscarRecursoDisponible(List<Resource> recursos, Reservation nuevaReserva, List<Reservation> reservasExistentes) {
        LocalTime nuevaInicio = LocalTime.parse(nuevaReserva.getStartTime());
        LocalTime nuevaFin = LocalTime.parse(nuevaReserva.getEndTime());

        for (Resource res : recursos) {
            boolean ocupado = false;

            for (Reservation r : reservasExistentes) {
                if ("ACTIVA".equalsIgnoreCase(r.getStatus()) && r.getDate().equals(nuevaReserva.getDate())) {

                    boolean tieneRecurso = r.getResources() != null &&
                            r.getResources().stream().anyMatch(rec -> rec.getId().equals(res.getId()));

                    if (tieneRecurso) {
                        LocalTime existInicio = LocalTime.parse(r.getStartTime());
                        LocalTime existFin = LocalTime.parse(r.getEndTime());


                        if (nuevaInicio.isBefore(existFin) && nuevaFin.isAfter(existInicio)) {
                            ocupado = true;
                            break;
                        }
                    }
                }
            }

            if (!ocupado) {
                return res;
            }
        }

        return null;
    }

    public String generateNextId(List<Reservation> reservas) {
        int max = 0;
        for (Reservation r : reservas) {
            if (r.getId() != null) {
                String num = r.getId().replace("RES-", "");
                try {
                    int valor = Integer.parseInt(num);
                    if (valor > max) {
                        max = valor;
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("RES-%03d", max + 1);
    }

   /* public static String generateNextId(int lastNumber) {
        return String.format("RES-%03d", lastNumber + 1);
    }*/

    public List<Reservation> findAllResources() throws Exception{
        return reservationDAO.findAll();
    }

    public ReservaExtraccion extraerReserva(String frase) throws Exception {
        List<Category> categories = categoryDAO.findAll();

        String listaCategorias = categories.stream()
                .map(Category::getDescription)
                .collect(Collectors.joining(", "));

        OpenAiChatModel aiModel = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        ReservaExtractorService aiService = AiServices.create(ReservaExtractorService.class, aiModel);

        return aiService.extraer(frase, listaCategorias, LocalTime.now().toString());
    }

    public void delete(String id) throws Exception {
        reservationDAO.delete(id);
    }

    public void cancel(String id) throws Exception {
       Reservation existe;
       try{
           existe = reservationDAO.read(id);
       }catch (Exception ex){
           throw new Exception("No se encontro la reservacion solicitada.");
       }

        if ("CANCELADA".equalsIgnoreCase(existe.getStatus())) {
            throw new Exception("La reservación ya se encuentra cancelada.");
        }

        existe.setStatus("CANCELADA");
        reservationDAO.update(existe);
    }
}
