package IIProyectoP3.presentation.alertas;

import IIProyectoP3.presentation.AbstractTableModel;
import domain.Reservation;
import domain.Resource;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.util.List;
import java.util.stream.Collectors;

public class AlertasTableModel extends AbstractTableModel<Reservation> {
    public static final int ACTIVIDAD = 0;
    public static final int VER = 1;
    public static final int DESCRIPCION = 2;


    private final Icon iconVer = new ImageIcon(getClass().getResource("images/iconVer.png"));
    private final Icon iconDesc = new ImageIcon(getClass().getResource("images/iconDesc.png"));

    public AlertasTableModel(int[] cols, List<Reservation> rows) {
        super(cols, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[3];
        colNames[ACTIVIDAD] = "Actividad";
        colNames[VER] = "Ver";
        colNames[DESCRIPCION] = "Desc.";
    }

    @Override
    protected Object getPropertyAt(Reservation re, int col) {
        switch (cols[col]) {
            case ACTIVIDAD:
                return re.getActivity();
            case VER:
                return iconVer;
            case DESCRIPCION:
                return iconDesc;
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (cols[columnIndex] == VER || cols[columnIndex] == DESCRIPCION) {
            return Icon.class;
        }
        return super.getColumnClass(columnIndex);
    }
}