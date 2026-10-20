package IIProyectoP3.presentation.statistics;

import IIProyectoP3.presentation.AbstractTableModel;
import domain.ActivityStatistics;

import java.util.List;

public class ActivitiesTableModel extends AbstractTableModel<ActivityStatistics>{
    public static final int SEMANA = 0;
    public static final int CANTIDAD = 1;

    public ActivitiesTableModel(int[] cols, List<ActivityStatistics> rows){
        super(cols, rows);
    }

    @Override
    protected void initColNames(){
        colNames = new String[2];
        colNames[SEMANA] = "Semana";
        colNames[CANTIDAD] = "Cantidad";
    }

    @Override
    protected Object getPropertyAt(ActivityStatistics e, int col) {
        switch(cols[col]){
            case SEMANA: return e.getSemana();
            case CANTIDAD: return e.getCantidad();
            default: return null;
        }
    }

}
