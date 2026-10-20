package IIProyectoP3.presentation.employees;

import domain.Employee;
import IIProyectoP3.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

public class  EmployeeView implements  PropertyChangeListener{
    private JTabbedPane tabbedPane1;
    private JPanel Panel;
    private JTextField nombreFld;
    private JButton buscarButton;
    private JButton ImprimirButton; // Ojo con la 'I' mayúscula tal como sale en tu imagen
    private JTextField idFld;
    private JTextField idfuncionarios;
    private JTextField nombreFuncionarios;
    private JTextField telefono;
    private JTable table1;
    private JButton guardarButton;
    private JButton borrarButton;
    private JButton limpiarButton;
    private JPanel principalPanel;

    private EmployeeModel model;
    private EmployeeController controller;

    public EmployeeView() {
        // Inicialización básica y carga de íconos para las pestañas
        cargarIconosPestanias();

        idfuncionarios.setEditable(false);

        table1.setRowHeight(35);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table1.getSelectedRow();
                if (row >= 0){
                    controller.edit(row);
                }
            }
        });


        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idFld.getText().trim();
                String nombre = nombreFld.getText().trim();

                if (!id.isEmpty()){
                    controller.search(id,true);
                }else{
                    if (nombre.isEmpty()){
                        JOptionPane.showMessageDialog(Panel,"Debe ingresar nombre o ID");
                    }
                    controller.search(nombre,false);
                }

                if (model.getList().isEmpty()){
                    JOptionPane.showMessageDialog(Panel,"No se encontro el funcionario");
                }
            }
        });

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validate()){
                    Employee emp = take();
                    try {
                        controller.create(emp);
                        JOptionPane.showMessageDialog(Panel,"Funcionario guardado correctamente");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(Panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idfuncionarios.getText().trim();
                if (id.isEmpty()){
                    JOptionPane.showMessageDialog(Panel,"Seleccionar Funcionario");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(Panel,"Seguro/a que desea borrar al funcionario " + id + "?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                try{
                    controller.delete(id);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });

        ImprimirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.print();
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(Panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private Employee take(){
        Employee emp = new Employee();
        emp.setId(idfuncionarios.getText().trim());
        emp.setName(nombreFuncionarios.getText().trim());
        emp.setPhone(telefono.getText().trim());
        return emp;
    }

    private boolean validate(){
        boolean valid = true;

        if (nombreFuncionarios.getText().trim().isEmpty()){
            valid = false;
            nombreFuncionarios.setBackground(Main.BACKGROUND_ERROR);
            nombreFuncionarios.setToolTipText("Se requiere el campo de Nombre");
        }else {
            nombreFuncionarios.setBackground(null);
            nombreFuncionarios.setToolTipText(null);
        }

        return valid;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        switch (evt.getPropertyName()){
            case (EmployeeModel.LIST):
                int[] cols = {EmployeeTableModel.ID,EmployeeTableModel.NOMBRE,EmployeeTableModel.TELEFONO };
                table1.setModel(new EmployeeTableModel(cols,model.getList()));
                break;
            case EmployeeModel.CURRENT:
                Employee curr = model.getCurrent();
                idfuncionarios.setText(curr.getId() ==  null ? "": curr.getId());
                nombreFuncionarios.setText(curr.getName() ==  null ? "": curr.getName());
                telefono.setText(curr.getPhone() ==  null ? "": curr.getPhone());
                break;
        }
    }

    private ImageIcon getIcono(String ruta) {
        URL url = getClass().getResource(ruta);
        return (url != null) ? new ImageIcon(url) : null;
    }

    private void cargarIconosPestanias() {
        if (tabbedPane1 != null) {
            tabbedPane1.setIconAt(0, getIcono("/icons/reserva.png"));
            // tabbedPane1.setIconAt(1, getIcono("/icons/calendar.png"));
            // tabbedPane1.setIconAt(2, getIcono("/icons/actividades.png"));
            // tabbedPane1.setIconAt(3, getIcono("/icons/estadisticas.png"));
        }
    }

    public JPanel getPanel() {
        return principalPanel;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane1;
    }

    public EmployeeModel getModel() {
        return model;
    }

    public void setModel(EmployeeModel model) {
        this.model = model;
        if (this.model != null){
            this.model.addPropertyChangeListener(this);
        }
    }

    public EmployeeController getController() {
        return controller;
    }

    public void setController(EmployeeController controller) {
        this.controller = controller;
    }
}