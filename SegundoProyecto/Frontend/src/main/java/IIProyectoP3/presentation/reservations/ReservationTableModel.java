package IIProyectoP3.presentation.reservations;

import domain.Reservation;
import domain.Resource;
import IIProyectoP3.presentation.AbstractTableModel;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationTableModel extends AbstractTableModel<Reservation> {
    public static final int ID = 0;
    public static final int ACTIVIDAD= 1;
    public static final int FECHA= 2;
    public static final int HORARIO= 3;
    public static final int RECURSO = 4;
    public static final int ESTADO = 5;

    public ReservationTableModel(int[] cols, List<Reservation> rows) {
        super(cols, rows);
    }
    @Override
    protected void initColNames() {
        colNames = new String[6];
        colNames[ID] = "Id";
        colNames[ACTIVIDAD] = "Actividad";
        colNames[FECHA] = "Fecha";
        colNames[HORARIO] = "Horario";
        colNames[RECURSO] = "Recurso";
        colNames[ESTADO] = "Estado";

    }

    @Override
    protected Object getPropertyAt(Reservation re, int col) {
        switch (cols[col]) {
            case ID: return re.getId();
            case ACTIVIDAD: return re.getActivity();
            case FECHA: return re.getDate();
            case HORARIO:
                String inicio = (re.getStartTime() != null) ? re.getStartTime() : "";
                String fin = (re.getEndTime() != null) ? re.getEndTime() : "";
                return inicio + " - " + fin;
            case RECURSO:
                if (re.getResources() != null && !re.getResources().isEmpty()) {
                    return re.getResources().stream()
                            .map(Resource::getId)
                            .collect(Collectors.joining(", "));
                }
                return "Sin Recursos";
            case ESTADO: return re.getStatus();
            default: return null;
        }
    }
}
