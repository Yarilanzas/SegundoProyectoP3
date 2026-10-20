package IIProyectoP3.presentation.login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Login extends JDialog implements PropertyChangeListener {
    private JPanel contentPane;
    private JPanel PrincipalPanel;
    private JTextField idtextField1;
    private JTextField clavetextField;
    private JButton ingresarButton;
    private JButton cancelarButton;
    private JButton cambiarButton;

    ControllerLogin controller;
    ModelLogin model;

    public Login() {
        setContentPane(contentPane);
        setModal(true);

        ingresarButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateJTextField(idtextField1)  && validateJTextField(clavetextField)) {
                    try {
                        controller.login(idtextField1.getText().trim(), clavetextField.getText().trim());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(contentPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Debe ingresar un ID y una clave", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                }
            }
        });



        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });



        cambiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateJTextField(idtextField1)) {
                    try {
                        controller.changePassword(idtextField1.getText() );
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(contentPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Debe ingresar un ID poder cambiar la contraseña", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                }
                /*try {
                    controller.changePassword(ClaveActualtextField1.getText().trim(), clavetextField.getText().trim());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(contentPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }*/
            }
        });
    }

    public void setController(ControllerLogin controller) {
        this.controller = controller;
    }

    public JPanel getPanel() {
        return contentPane;
    }

    public void setModel(ModelLogin model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    private void onCancel() {
        dispose();
    }

    private boolean validateJTextField(JTextField name) {
        return !name.getText().trim().isEmpty();
    }
   /* public User takeUser(){

        if (validateJTextField(idtextField1)  && validateJTextField(clavetextField)) {
            try {
               u.setId(idtextField1.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(contentPane, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(contentPane, "Debe ingresar un ID y una clave", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
        }
    }*/

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}

/*// Acción al cerrar la ventana con la X
setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent e) {
        onCancel();
    }
});*/