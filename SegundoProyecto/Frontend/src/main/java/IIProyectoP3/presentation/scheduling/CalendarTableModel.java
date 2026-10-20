package IIProyectoP3.presentation.scheduling;

import domain.CalendarData;
import domain.Resource;

import javax.swing.table.AbstractTableModel;

public class CalendarTableModel extends AbstractTableModel {
    private final CalendarData data;

    public CalendarTableModel(CalendarData data){
        this.data = data;
    }

    @Override
    public int getRowCount(){
        return data.getHours().size();
    }

    @Override
    public int getColumnCount(){
        return 1 + data.getResources().size();
    }

    @Override
    public String getColumnName(int col){
        if (col == 0) return "Hora";
        return data.getResources().get(col - 1).getDescription();
    }

    @Override
    public Object getValueAt(int row, int col){
        String hora = data.getHours().get(row);
        if (col == 0) return hora;
        Resource recurso = data.getResources().get(col - 1 );
        return data.getCell(hora,recurso.getId());
    }
}
