package IIProyectoP3.presentation.scheduling;

import domain.CalendarData;
import domain.Category;
import IIProyectoP3.presentation.AbstractModel;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class SchedulingModel extends AbstractModel {
    public static final String CATEGORIES = "categories";
    public static final String CALENDAR = "calendar";

    private List<Category> categories;
    private CalendarData calendarData;

    public SchedulingModel() {
        categories = new ArrayList<>();
        calendarData = null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener){
        super.addPropertyChangeListener(listener);
        firePropertyChange(CATEGORIES);
    }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories){
        this.categories = categories;
        firePropertyChange(CATEGORIES);
    }

    public CalendarData getCalendarData() { return calendarData; }
    public void setCalendarData(CalendarData calendarData){
        this.calendarData = calendarData;
        firePropertyChange(CALENDAR);
    }
}
