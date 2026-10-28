package IIProyectoP3.presentation.alertas;

import IIProyectoP3.presentation.AbstractModel;
import domain.Reservation;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;



import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class AlertasModel extends AbstractModel {
    List<Reservation> reservas;

    public static final String RESERVAS = "reservas";

    public AlertasModel() {
        reservas = new ArrayList<Reservation>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(RESERVAS);
    }

    public List<Reservation> getList() {
        return reservas;
    }

    public void setList(List<Reservation> reservas) {
        this.reservas = reservas;
        firePropertyChange(RESERVAS);
    }
}
