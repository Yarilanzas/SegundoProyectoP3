package IIProyectoP3.presentation.alertas;

import javax.swing.*;

public class MostrarReservaView {
    private JList listaRecursos;
    private JLabel labelEncargado;
    private JLabel fechaLabel;
    private JPanel panel;
    private JLabel desdeLabel;
    private JLabel hastaLabel;

    ResourceListModel resourceListModel = new ResourceListModel();
    public MostrarReservaView() {
        resourceListModel = new ResourceListModel();
        listaRecursos.setModel(resourceListModel.getListModel());
    }

    public JPanel getPanel() {
        return panel;
    }

    public JList getListaRecursos() {
        return listaRecursos;
    }

    public void setListaRecursos(JList listaRecursos) {
        this.listaRecursos = listaRecursos;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JLabel getFechaLabel() {
        return fechaLabel;
    }

    public JLabel getDesdeLabel() {
        return desdeLabel;
    }

    public JLabel getHastaLabel() {
        return hastaLabel;
    }

    public void setFechaLabel(JLabel fechaLabel) {
        this.fechaLabel = fechaLabel;
    }

    public JLabel getLabelEncargado() {
        return labelEncargado;
    }

    public void setLabelEncargado(JLabel labelEncargado) {
        this.labelEncargado = labelEncargado;
    }


}
