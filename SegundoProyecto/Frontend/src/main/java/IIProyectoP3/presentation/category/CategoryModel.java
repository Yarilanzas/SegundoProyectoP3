package IIProyectoP3.presentation.category;

import domain.Category;
import IIProyectoP3.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class CategoryModel extends AbstractModel {
    private Category current;
    private List<Category> list;

    public static final String CURRENT = "currentCategory";
    public static final String LIST = "listCategory";

    public CategoryModel() {
        current = new Category();
        list = new ArrayList<>();
    }

    public Category getCurrent() {
        return current;
    }

    public void setCurrent(Category current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public List<Category> getList() {
        return list;
    }

    public void setList(List<Category> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(LIST);

    }
}