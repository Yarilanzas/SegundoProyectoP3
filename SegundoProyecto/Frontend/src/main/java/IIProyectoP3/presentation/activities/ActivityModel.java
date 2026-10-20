package IIProyectoP3.presentation.activities;

import domain.ActivityData;
import IIProyectoP3.presentation.AbstractModel;


import java.beans.PropertyChangeListener;

public class ActivityModel extends AbstractModel{
    public static final String HORARIO = "schedule";
    private ActivityData activityData;

    public ActivityModel() {
        activityData = null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener){
        super.addPropertyChangeListener(listener);
        //no hay datos hasta que se cargue
    }

    public ActivityData getActivityData() {
        return activityData;
    }

    public void setActivityData(ActivityData activityData) {
        this.activityData = activityData;
        firePropertyChange(HORARIO);
    }
}
