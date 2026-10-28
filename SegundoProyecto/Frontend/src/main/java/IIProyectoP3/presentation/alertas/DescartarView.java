package IIProyectoP3.presentation.alertas;

import javax.swing.*;

public class DescartarView {
    private JLabel NombreReserva;
    private JButton noButton;
    private JButton yesButton;
    private JPanel panel;

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JLabel getNombreReserva() {
        return NombreReserva;
    }

    public void setNombreReserva(JLabel nombreReserva) {
        NombreReserva = nombreReserva;
    }
}
