package IIProyectoP3.presentation.login;

import domain.User;
import IIProyectoP3.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
public class ModelLogin extends AbstractModel  {
    User current;
    public static final String CURRENT = "current";

    public ModelLogin() {
        current = new User() {
        };
    }
    public void setCurrent(User current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
    }
}
