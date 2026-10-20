package IIProyectoP3.presentation.activities;

import domain.ActivityData;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ActivityTableModel extends AbstractTableModel {
    private final ActivityData data;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("EEE yyyy-MM-dd");

    public ActivityTableModel(ActivityData data) {
        this.data = data;
    }

    @Override
    public int getRowCount(){
        return data.getHours().size();
    }

    @Override
    public int getColumnCount(){
        return 1 + data.getDays().size();
    }

    @Override
    public String getColumnName(int col){
        if (col == 0) return "Hora";
        return data.getDays().get(col - 1).format(FMT);
    }

    @Override
    public Object getValueAt(int row, int col){
        String hora = data.getHours().get(row);
        if (col == 0) return hora;
       LocalDate dia = data.getDays().get(col - 1 );
        return data.getCell(hora,dia);
    }
}
