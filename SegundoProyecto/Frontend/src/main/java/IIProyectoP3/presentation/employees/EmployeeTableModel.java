package IIProyectoP3.presentation.employees;

import domain.Employee;
import IIProyectoP3.presentation.AbstractTableModel;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel<Employee> {

    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int TELEFONO = 2;

    public EmployeeTableModel(int[] cols, List<Employee> rows){
        super(cols, rows);
    }

    @Override
    protected void initColNames(){
        colNames = new String[3];
        colNames[ID] = "Id";
        colNames[NOMBRE] = "Nombre";
        colNames[TELEFONO] = "Telefono";
    }

    @Override
    protected Object getPropertyAt(Employee e, int col) {
        switch(cols[col]){
            case ID: return e.getId();
            case NOMBRE: return e.getName();
            case TELEFONO: return e.getPhone();
            default: return null;
        }
    }
}