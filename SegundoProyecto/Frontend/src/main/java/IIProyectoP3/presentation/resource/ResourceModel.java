package IIProyectoP3.presentation.resource;

import domain.Category;
import domain.Resource;
import IIProyectoP3.presentation.AbstractModel;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ResourceModel extends AbstractModel {
    Resource current;
    List<Category> categories;
    List<Resource> resources;


    public static final String CATEGORIES = "categories";
    public static final String CURRENT= "current";
    public static final String RESOURCES= "resources";


    public ResourceModel() {
        current = new Resource();
        categories = new ArrayList<Category>();
        resources = new ArrayList<Resource>();

    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(CATEGORIES);
        firePropertyChange(RESOURCES);

    }

    public Resource getCurrent() {
        return current;
    }

    public void setCurrent(Resource current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        firePropertyChange(CATEGORIES);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
        firePropertyChange(RESOURCES);
    }



}
