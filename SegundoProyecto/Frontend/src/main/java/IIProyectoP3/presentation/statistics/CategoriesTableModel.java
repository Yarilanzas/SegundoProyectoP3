package IIProyectoP3.presentation.statistics;

import IIProyectoP3.presentation.AbstractTableModel;

import java.util.List;
import domain.CategoryStatistics;


public class CategoriesTableModel  extends AbstractTableModel<CategoryStatistics> {
    public static final int CATEGORIA = 0;
    public static final int CANTIDAD = 1;

    public CategoriesTableModel(int[] cols, List<CategoryStatistics> rows){
        super(cols, rows);
    }

    @Override
    protected void initColNames(){
        colNames = new String[2];
        colNames[CATEGORIA] = "Categoria";
        colNames[CANTIDAD] = "Cantidad";
    }

    @Override
    protected Object getPropertyAt(CategoryStatistics e, int col) {
        switch(cols[col]){
            case CATEGORIA: return e.getCategory().getDescription();
            case CANTIDAD: return e.getCantidad();
            default: return null;
        }
    }
}
