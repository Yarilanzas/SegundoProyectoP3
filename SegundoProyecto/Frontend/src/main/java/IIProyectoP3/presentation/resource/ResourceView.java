package IIProyectoP3.presentation.resource;

import domain.Category;
import domain.Resource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ResourceView implements PropertyChangeListener {
    private JButton imprimirButton2;
    private JButton buscarButton2;
    private JComboBox comboBoxCategorias;
    private JTextField textField4;
    private JTextField IDRecursofld;
    private JTextField DescripcionRecursofld;
    private JButton guardarButton2;
    private JButton borrarButton2;
    private JButton limpiarButton2;
    private JPanel panel;
    private JPanel principalPanel;
    private JTable ResourceTable;
    private JComboBox RecursoCategoriafld;


    //private JPanel ResourceTable;

    private ResourceModel model;
    private ResourceController controller;

    public ResourceView() {

        ResourceTable.setRowHeight(35);
        ResourceTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);


        textField4.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void evaluar() {
                boolean tieneTexto = !textField4.getText().trim().isEmpty();
                comboBoxCategorias.setEnabled(!tieneTexto);
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { evaluar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { evaluar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { evaluar(); }
        });
        buscarButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String descripcion = textField4.getText().trim().toUpperCase();
                    Category categoriaSeleccionada = (Category) comboBoxCategorias.getSelectedItem();

                    if (descripcion.isEmpty() && categoriaSeleccionada == null) {
                        JOptionPane.showMessageDialog(principalPanel, "Debe ingresar una descripción o seleccionar una categoría.", "Atención", JOptionPane.WARNING_MESSAGE);
                        controller.list();
                        return;
                    }

                    if (!descripcion.isEmpty()) {
                        controller.searchbyDes(descripcion);
                    } else if (categoriaSeleccionada != null) {
                        controller.searchbyCategory(categoriaSeleccionada.getDescription());
                    }

                    if (model.getResources() == null || model.getResources().isEmpty()) {
                        JOptionPane.showMessageDialog(principalPanel, "No se encontraron recursos con los criterios especificados.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(principalPanel, "Error al buscar recursos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        guardarButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.saveResource(takeResoruce());
                    JOptionPane.showMessageDialog(principalPanel, "Recurso guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(principalPanel, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        limpiarButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField4.setText("");
                comboBoxCategorias.setSelectedIndex(-1);

                textField4.setEnabled(true);
                comboBoxCategorias.setEnabled(true);

                if (controller != null) {
                    controller.clear();
                }
            }
        });
        borrarButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = IDRecursofld.getText().trim();
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

        ResourceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = ResourceTable.getSelectedRow();
                if (row >= 0 && controller != null) {
                    controller.edit(row);
                }
            }
        });
        imprimirButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.print();
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(principalPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        comboBoxCategorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean tieneSeleccion = comboBoxCategorias.getSelectedItem() != null && comboBoxCategorias.getSelectedIndex() != -1;
                textField4.setEnabled(!tieneSeleccion);
            }
        });
    }
    public Resource takeResoruce()throws Exception {
        Resource r= new Resource();
        if (!validateJTextField(IDRecursofld)) {
            IDRecursofld.requestFocus();
            throw new Exception("Debe ingresar un ID para el recurso.");
        }

        if (!validateJTextField(DescripcionRecursofld)) {
            DescripcionRecursofld.requestFocus();
            throw new Exception("Debe ingresar una descripción para el recurso.");
        }

        if (RecursoCategoriafld.getSelectedItem() == null) {
            RecursoCategoriafld.requestFocus();
            throw new Exception("Debe seleccionar una categoría para el recurso.");
        }
        r.setId(IDRecursofld.getText().trim());
        r.setCategory((Category) RecursoCategoriafld.getSelectedItem());
        r.setDescription(DescripcionRecursofld.getText().trim());
        return  r;
    }
    public JPanel getPanel() {
        return principalPanel;
    }

    public void setPanel(JPanel panel) {
        this.principalPanel = panel;
    }

    public ResourceController getController() {
        return controller;
    }

    public void setController(ResourceController controller) {
        this.controller = controller;
    }

    public ResourceModel getModel() {
        return model;
    }

    public void setModel(ResourceModel model) {
        this.model = model;
        this.model.addPropertyChangeListener(this);
    }

@Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ResourceModel.CATEGORIES:
                DefaultComboBoxModel<Category> modelo = new DefaultComboBoxModel<>();
                DefaultComboBoxModel<Category> modelo2 = new DefaultComboBoxModel<>();

                for (Category c : model.getCategories()){
                    modelo.addElement(c);
                    modelo2.addElement(c);

                }
                comboBoxCategorias.setModel(modelo);
                RecursoCategoriafld.setModel(modelo2);
                comboBoxCategorias.setSelectedIndex(-1);
                RecursoCategoriafld.setSelectedIndex(-1);

                break;

            case ResourceModel.RESOURCES:
                int[] colum = {ResourceTableModel.ID, ResourceTableModel.CATEGORIA, ResourceTableModel.DESCRIPCION};
                ResourceTable.setModel(new ResourceTableModel(colum, model.getResources()));
                break;
            case ResourceModel.CURRENT:
                Resource curr = model.getCurrent();
                IDRecursofld.setText(curr.getId() == null ? "" : curr.getId());
                DescripcionRecursofld.setText(curr.getDescription() == null ? "" : curr.getDescription());

                RecursoCategoriafld.setSelectedItem(curr.getCategory());


                if (curr.getCategory() != null) {
                    ComboBoxModel<Category> comboModel = RecursoCategoriafld.getModel();
                    for (int i = 0; i < comboModel.getSize(); i++) {
                        Category item = comboModel.getElementAt(i);
                        if (item.getId().equals(curr.getCategory().getId())) {
                            RecursoCategoriafld.setSelectedIndex(i);
                            break;
                        }
                    }
                } else {
                    RecursoCategoriafld.setSelectedIndex(-1);
                }

                break;
        }
        this.principalPanel.revalidate();
    }

    private boolean validateJTextField(JTextField field) {
        return field != null && !field.getText().trim().isEmpty();
    }
}

