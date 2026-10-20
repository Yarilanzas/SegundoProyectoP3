package IIProyectoP3.presentation.employees;

import domain.Employee;
import IIProyectoP3.presentation.AbstractModel;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel extends AbstractModel {
    public static final String LIST = "list";
    public static final String CURRENT = "current";

    private List<Employee> list;
    private Employee current = new Employee();

    public EmployeeModel(){
        current = new Employee();
        list = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener){
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);
    }

    public List<Employee> getList() { return list; }
    public void setList (List<Employee> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public Employee getCurrent() { return current; }
    public void setCurrent(Employee current){
        this.current = current;
        firePropertyChange(CURRENT);
    }
}