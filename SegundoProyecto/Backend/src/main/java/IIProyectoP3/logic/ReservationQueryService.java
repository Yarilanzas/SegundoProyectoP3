/*Este service agrupa todas las consultas sobre reservaciones ya hechas: calendarización,
actividades y estadísticas. Son distintas formas de leer y resumir los mismos datos de
Reservation. Como los 4 métodos (getCalendar, getHorarioSemanal, getResourceStatistics,
getActivityStatistics) leen exactamente la misma fuente de datos, tenerlos juntos evita
duplicar reservationDAO.findAll() en 4 services distintos sin necesidad real de separarlos.
*/

package IIProyectoP3.logic;

import  IIProyectoP3.data.*;
import domain.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class ReservationQueryService {
    private static final String[] HORAS = {"06:00","07:00","08:00","09:00","10:00",
            "11:00","12:00","13:00","14:00", "15:00","16:00","17:00","18:00", "19:00",
            "20:00","21:00","22:00","23:00"};

    private final ReservationDAO reservationDAO;
    private final ResourceDAO resourceDAO;
    private final CategoryDAO categoryDAO;

    public ReservationQueryService(){
        this.reservationDAO = new ReservationDAO();
        this.resourceDAO = new ResourceDAO();
        this.categoryDAO = new CategoryDAO();
    }

    public CalendarData getCalendar(LocalDate fecha,Category categoria) throws Exception{
        if (fecha == null) throw new Exception("Debe seleccionar una fecha");
        if (categoria == null) throw new Exception("Debe seleccionar una categoría");

        List<Resource> recursos = resourceDAO.findByCategory(categoria.getId());
        List<Reservation> reservas = reservationDAO.findAll();

        List<String> horas = Arrays.asList(HORAS);
        Map<String,Map<String,String>> celdas = new HashMap<>();

        for (Reservation res : reservas){
            if (!"ACTIVA".equalsIgnoreCase(res.getStatus())) continue;
            if (!fecha.equals(res.getDate())) continue;

            String nomEmp = (res.getEmployee() != null) ? res.getEmployee().getName() : "No asignado";
            String texto = res.getActivity() + " - " + nomEmp;

            List<Resource> asignados = (res.getResources() != null) ? res.getResources() : new ArrayList<>();

            for (Resource asignado : asignados) {
                boolean categoriaseleccionada = recursos.stream().anyMatch(r -> r.getId().equals(asignado.getId()));

                if (!categoriaseleccionada) continue;

                for (String hora : horas){
                    if (horaEnRango(hora,res.getStartTime(), res.getEndTime())){
                        celdas.computeIfAbsent(hora, h -> new HashMap<>())
                                .put(asignado.getId(),texto);
                    }
                }
            }
        }
        return new CalendarData(horas,recursos,celdas);
    }

    private boolean horaEnRango(String hora,String inicio, String fin){
        //DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        //obj para formatear las horas a algun patron, appendPattern("[H:mm:ss][HH:mm:ss][H:mm][HH:mm]") essto
        //añade los patrones que puede seguir una fecha y lo intenta con todos hasta que coincida con alguno
        DateTimeFormatter formatter= new DateTimeFormatterBuilder().appendPattern("[H:mm:ss][HH:mm:ss][H:mm][HH:mm]").toFormatter();

        LocalTime h = LocalTime.parse(hora,formatter);
        LocalTime in = LocalTime.parse(inicio,formatter);
        LocalTime finn = LocalTime.parse(fin,formatter);
        return !h.isBefore(in) && h.isBefore(finn);
    }

    public ActivityData getHorarioSemanal(LocalDate fecharef) throws Exception{
        if (fecharef == null) throw new Exception("Debe seleccionar una fecha");

        LocalDate lunes = fecharef.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); //agarra el dia lunes
        List<LocalDate> dias = new ArrayList<>();
        for(int i = 0; i<7;i++){
            dias.add(lunes.plusDays(i)); //va agregando los demas dias y crea una ;ista de
        }

        List<Reservation> reservas = reservationDAO.findAll();
        List<String> horas = Arrays.asList(HORAS);
        Map<String, Map<LocalDate,String>> celdas = new HashMap<>();

        for (Reservation reserva : reservas){
            if (!"ACTIVA".equalsIgnoreCase(reserva.getStatus())) continue;
            if (!dias.contains(reserva.getDate())) continue;

            String nomEmp = (reserva.getEmployee()!=null) ? reserva.getEmployee().getName() : "No asignado";

            for (String hora : horas){
                if (horaEnRango(hora, reserva.getStartTime(), reserva.getEndTime())){
                    celdas.computeIfAbsent(hora, h -> new HashMap<>())
                            .put(reserva.getDate(), reserva.getActivity() + " (" + nomEmp + ")");
                }
            }
        }
        return new ActivityData(dias, horas,celdas);
    }


    public List <CategoryStatistics> getResourceStatistics(LocalDate desde, LocalDate hasta) throws Exception{
        if (desde == null || hasta == null) throw new Exception("Debe seleccionar el rango de fechas");
        if (desde.isAfter(hasta)) throw new Exception("La fecha 'Desde' no puede ser posterior a 'Hasta'");

        List<Category> categories = categoryDAO.findAll();
        List<Reservation> reservas = reservationDAO.findAll();

        List<CategoryStatistics> estadisticas = new ArrayList<>();
        for (Category c : categories){
            estadisticas.add(new CategoryStatistics(c,0));
        }

        for (Reservation reserva : reservas){
            if (!"ACTIVA".equalsIgnoreCase(reserva.getStatus())) continue;
            if (reserva.getDate().isBefore(desde) || reserva.getDate().isAfter(hasta)) continue;

            List<Resource> asignados = (reserva.getResources() != null) ? reserva.getResources() : new ArrayList<>();

            for (Resource recurso : asignados){
                if (recurso.getCategory() == null) continue;
                String categoriaId = recurso.getCategory().getId();

                for (CategoryStatistics ce: estadisticas){
                    if (ce.getCategory().getId().equals(categoriaId)){
                        ce.setCantidad(ce.getCantidad() + 1);
                    }
                }
            }
        }
        return estadisticas;
    }

    public List<ActivityStatistics> getActivityStatistics(LocalDate desde, LocalDate hasta) throws Exception{
        if (desde == null || hasta == null) throw new Exception("Debe seleccionar el rango de fechas");
        if (desde.isAfter(hasta)) throw new Exception("La fecha 'Desde' no puede ser posterior a 'Hasta'");

        List<Reservation> reservas = reservationDAO.findAll();

        List<ActivityStatistics> estadisticas = new ArrayList<>();
        List<LocalDate> fechas = new ArrayList<>();

        LocalDate lunes = desde.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (LocalDate date = lunes; !date.isAfter(hasta); date = date.plusDays(7)){
            estadisticas.add(new ActivityStatistics(date.toString(),0));
            fechas.add(date);
        }

        reservas.stream()
            .filter(r -> "ACTIVA".equalsIgnoreCase(r.getStatus()))
            .filter(r -> !r.getDate().isBefore(desde) && !r.getDate().isAfter(hasta))
            .forEach(r -> {
                for (int i = fechas.size() - 1; i>=0; i--){
                    if (!r.getDate().isBefore(fechas.get(i))){
                        ActivityStatistics actividad = estadisticas.get(i);
                        actividad.setCantidad(actividad.getCantidad() + 1);
                        break;
                    }
                }
            });
        return estadisticas;
    }
}
