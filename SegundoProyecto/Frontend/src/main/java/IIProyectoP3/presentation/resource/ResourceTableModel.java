package IIProyectoP3.presentation.resource;

import domain.Resource;
import IIProyectoP3.presentation.AbstractTableModel;
import java.util.List;


public class ResourceTableModel extends AbstractTableModel<Resource> {

    public static final int ID = 0;
    public static final int CATEGORIA = 1;

    public static final int DESCRIPCION = 2;

    public ResourceTableModel(int[] cols, List<Resource> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ID] = "Id";
        colNames[CATEGORIA] = "Categoria";
        colNames[DESCRIPCION] = "Descripcion";
    }

    @Override
    protected Object getPropertyAt(Resource re, int col) {
        switch (cols[col]) {
            case ID: return re.getId();
            case CATEGORIA: return re.getCategory();
            case DESCRIPCION: return re.getDescription();
            default: return null;
        }
    }
}