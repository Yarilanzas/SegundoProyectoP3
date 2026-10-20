package IIProyectoP3.presentation.category;

import domain.Category;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CategoryView implements PropertyChangeListener {
    private JPanel panel;
    private JTable tableCategories;
    private JTextField idCategoriaFld;
    private JTextField descripcionCategoriaFld;
    private JButton guardarButton1;
    private JButton borrarButton1;
    private JButton limpiarButton1;
    private JTextField descripcionFld;
    private JButton imprimirButton1;
    private JButton buscarButton1;
    private JPanel principalPanel;

    private CategoryModel model;
    private CategoryController controller;

    public CategoryView() {

        tableCategories.setRowHeight(35);
        tableCategories.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        guardarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // SOLO validamos que la descripción no esté vacía
                if (validateJTextField(descripcionCategoriaFld)) {
                    try {
                        controller.saveCategory(takeCategory());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(principalPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(principalPanel, "Asegúrese de ingresar una descripción", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        tableCategories.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableCategories.getSelectedRow();
                if (row >= 0 && controller != null) {
                    controller.edit(row);
                }
            }
        });


        borrarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idCategoriaFld.getText().trim();
                if (id.isEmpty()){
                    JOptionPane.showMessageDialog(principalPanel,"Debe ingresar el id para borrar");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(principalPanel,"Seguro/a que desea borrar la categoria " + id + "?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                try{
                    controller.delete(id);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(principalPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        limpiarButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });
        buscarButton1.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    String descripcion = (descripcionFld.getText().trim()).toUpperCase();

                    if (descripcion.isEmpty()){
                        JOptionPane.showMessageDialog(principalPanel,"Debe ingresar una descripcion");
                        controller.list();
                        return;
                    }else {
                        controller.searchDesc(descripcion);
                    }


                    if (model.getList().isEmpty()){
                        JOptionPane.showMessageDialog(principalPanel,"No se encontro ninguna categoria con esa descripcion");
                    }

            }
        });
        imprimirButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(principalPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            }
        });
        idCategoriaFld.setEditable(false);
    }

    private boolean validateJTextField(JTextField field) {
        return field != null && !field.getText().trim().isEmpty();
    }

    private Category takeCategory() {
        Category cat = new Category();
        cat.setId(idCategoriaFld.getText().trim());
        cat.setDescription(descripcionCategoriaFld.getText().trim().toUpperCase());
        return cat;
    }

    public void setModel(CategoryModel model) {
        this.model = model;
        if (this.model != null) {
            this.model.addPropertyChangeListener(this);
        }
    }

    public void setController(CategoryController controller) {
        this.controller = controller;
    }

    public JPanel getPanel() {
        return principalPanel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case CategoryModel.LIST:
                int[] colum = {CategoryTableModel.ID, CategoryTableModel.DESCRIPCION};
                tableCategories.setModel(new CategoryTableModel(colum, model.getList()));
                break;
            case CategoryModel.CURRENT:
                Category curr = model.getCurrent();
                idCategoriaFld.setText(curr.getId() == null ? "" : curr.getId());
                descripcionCategoriaFld.setText(curr.getDescription() == null ? "" : curr.getDescription());
                break;
        }
    }
}