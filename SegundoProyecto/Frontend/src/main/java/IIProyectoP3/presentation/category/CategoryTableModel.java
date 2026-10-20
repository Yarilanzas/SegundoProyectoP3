package IIProyectoP3.presentation.category;

import domain.Category;

import IIProyectoP3.presentation.AbstractTableModel;

import java.util.List;

public class CategoryTableModel extends AbstractTableModel<Category> {

    public static final int ID = 0;
    public static final int DESCRIPCION = 1;

    public CategoryTableModel(int[] cols, List<Category> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[2];
        colNames[ID] = "Id";
        colNames[DESCRIPCION] = "Descripcion";
    }

    @Override
    protected Object getPropertyAt(Category cat, int col) {
        switch (cols[col]) {
            case ID: return cat.getId();
            case DESCRIPCION: return cat.getDescription();
            default: return null;
        }
    }
}