package IIProyectoP3.presentation.changePassword;

import javax.swing.*;
import java.awt.event.*;

public class ChangePasswordView extends JDialog {
    private JPanel principalPanel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField ClaveActualtextField1;
    private JTextField ClaveNuevatextField1;
    private JTextField ConfirmacionClavetextField1;
    private ChangePasswordModel model;

    public ChangePasswordController getController() {
        return controller;
    }

    public void setController(ChangePasswordController controller) {
        this.controller = controller;
    }

    public ChangePasswordModel getModel() {
        return model;
    }

    public void setModel(ChangePasswordModel model) {
        this.model = model;
    }

    private ChangePasswordController controller;


    public ChangePasswordView() {
        setContentPane(principalPanel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        principalPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.change(
                            ClaveActualtextField1.getText(),
                            ClaveNuevatextField1.getText(),
                            ConfirmacionClavetextField1.getText()
                    );
                    JOptionPane.showMessageDialog(principalPanel, "Contraseña actualizada exitosamente");

                    SwingUtilities.getWindowAncestor(principalPanel).dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(principalPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public JPanel getPanel() {
        return principalPanel;
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ChangePasswordView dialog = new ChangePasswordView();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
