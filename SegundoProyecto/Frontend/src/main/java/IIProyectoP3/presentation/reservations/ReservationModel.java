package IIProyectoP3.presentation.reservations;

import domain.Category;
import domain.Reservation;
import IIProyectoP3.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ReservationModel extends AbstractModel {
    Reservation current;
    private List<Reservation> reservations;
    private List<Category> categories;

    public static final String CURRENT = "current";
    public static final String RESERVATIONS = "listReservations";
    public static final String CATEGORIES = "listCategories";


    public ReservationModel(){
        current= new Reservation();
        reservations= new ArrayList<Reservation>();
        categories= new ArrayList<Category>();

    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(RESERVATIONS);
        firePropertyChange(CATEGORIES);

    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        firePropertyChange(RESERVATIONS);

    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        firePropertyChange(CATEGORIES);

    }

    public void setCurrent(Reservation current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public Reservation getCurrent() {
        return current;
    }
}
