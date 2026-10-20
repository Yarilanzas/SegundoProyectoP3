package IIProyectoP3.presentation.scheduling;

import com.github.lgooddatepicker.components.DatePicker;
import domain.Category;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class SchedulingView implements PropertyChangeListener{
    private JPanel CalendarizacionPanel;
    private DatePicker fechaPicker;
    private JComboBox categoriaComboBox;
    private JButton cargarButton;
    private JTable Calendarizaciontable;
    private JPanel principalPanel;
    private JButton imprimirButton;

    private SchedulingModel model;
    private SchedulingController controller;


    public SchedulingView() {

        Calendarizaciontable.setRowHeight(32);
        Calendarizaciontable.setDefaultRenderer(Object.class,new javax.swing.table.DefaultTableCellRenderer(){
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
                    LocalDate fecha = fechaPicker.getDate();
                    Category categoria = (Category) categoriaComboBox.getSelectedItem();

                    if (fecha == null || categoria == null){
                        JOptionPane.showMessageDialog(CalendarizacionPanel,"Seleccione fecha y categoria");
                        return;
                    }
                    controller.cargarCalendar(fecha,categoria);
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(CalendarizacionPanel,ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        imprimirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.print();
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(CalendarizacionPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        categoriaComboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.cargarCategorias();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        switch(evt.getPropertyName()){
            case SchedulingModel.CATEGORIES:
                DefaultComboBoxModel<Category> modelo = new DefaultComboBoxModel<>();
                for (Category c : model.getCategories()){
                    modelo.addElement(c);
                }
                categoriaComboBox.setModel(modelo);
                break;
            case SchedulingModel.CALENDAR:
                if (model.getCalendarData() != null) {
                    Calendarizaciontable.setModel(new CalendarTableModel(model.getCalendarData()));
                }
                break;
        }
    }


    public JPanel getPanel() {
        return principalPanel;
    }
    public void setPanel(JPanel panel) {
        this.principalPanel = panel;
    }

    public SchedulingController getController() {
        return controller;
    }
    public void setController(SchedulingController controller) {
        this.controller = controller;
    }

    public SchedulingModel getModel() {
        return model;
    }
    public void setModel(SchedulingModel model) {
        this.model = model;
        if (this.model != null){
            this.model.addPropertyChangeListener(this);
        }
    }
}