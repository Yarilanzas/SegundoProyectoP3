package IIProyectoP3.presentation.activities;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class ActivityView implements PropertyChangeListener{
    private ActivityModel model;
    private ActivityController controller;
    private JPanel panel;
    private JPanel principalPanel;
    private DatePicker fechapicker;
    private JButton cargarButton;
    private JButton imprimirbutton;
    private JTable Actividadestable;


    public ActivityView() {
        Actividadestable.setRowHeight(40);
        Actividadestable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        Actividadestable.setDefaultRenderer(Object.class,new javax.swing.table.DefaultTableCellRenderer(){
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                java.awt.Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);

                if (column > 0 && value != null && !value.toString().trim().isEmpty()){
                    c.setBackground(new java.awt.Color(223, 149, 194));
                    c.setForeground(java.awt.Color.BLACK);
                }else {
                    c.setBackground(java.awt.Color.WHITE);
                    c.setForeground(java.awt.Color.BLACK);
                }

                if (isSelected){
                    c.setBackground(table.getSelectionBackground());
                }
                return c;
            }
        });


        cargarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    LocalDate fecha = fechapicker.getDate();

                    if (fecha == null){
                        JOptionPane.showMessageDialog(principalPanel,"Seleccione fecha de referencia");
                        return;
                    }
                    controller.cargarHorario(fecha);
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(principalPanel,ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        imprimirbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.print();
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(principalPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt){
       if (ActivityModel.HORARIO.equals(evt.getPropertyName())){
           Actividadestable.setModel(new ActivityTableModel(model.getActivityData()));
       }
    }

    public JPanel getPanel() {
        return principalPanel;
    }

    public void setPanel(JPanel panel) {
        this.principalPanel = panel;
    }

    public ActivityController getController() {
        return controller;
    }

    public void setController(ActivityController controller) {
        this.controller = controller;
    }

    public ActivityModel getModel() {
        return model;
    }

    public void setModel(ActivityModel model) {
        this.model = model;
        if (this.model != null){
            this.model.addPropertyChangeListener(this);
        }
    }
}
