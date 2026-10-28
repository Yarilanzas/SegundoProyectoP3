package IIProyectoP3.presentation.alertas;

import IIProyectoP3.logic.ServiceProxy;
import IIProyectoP3.presentation.SocketListener;
import IIProyectoP3.presentation.activities.ActivityController;
import IIProyectoP3.presentation.activities.ActivityModel;
import IIProyectoP3.presentation.activities.ActivityView;
import IIProyectoP3.presentation.reservations.ReservationController;
import IIProyectoP3.presentation.reservations.ReservationModel;
import IIProyectoP3.presentation.reservations.ReservationView;
import IIProyectoP3.presentation.scheduling.SchedulingController;
import IIProyectoP3.presentation.scheduling.SchedulingModel;
import IIProyectoP3.presentation.scheduling.SchedulingView;
import IIProyectoP3.presentation.statistics.StatisticsController;
import IIProyectoP3.presentation.statistics.StatisticsModel;
import IIProyectoP3.presentation.statistics.StatisticsView;
import domain.Reservation;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AlertasView implements PropertyChangeListener {
    private JPanel panel1;
    private JTable alertas;

    AlertasController controller;
    AlertasModel model;


    public AlertasView() {
        alertas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = alertas.getSelectedRow();
                int col = alertas.getSelectedColumn();

                if (row != -1) {
                    AlertasTableModel tableModel = (AlertasTableModel) alertas.getModel();
                    Reservation reserva = tableModel.getRowAt(row);

                    if (col == AlertasTableModel.VER) {
                        controller.mostrarDetalleReserva(reserva);
                    }

                    if (col == AlertasTableModel.DESCRIPCION) {
                        int resp = JOptionPane.showConfirmDialog(
                                panel1,
                                "¿Descartar la alerta de " + reserva.getActivity() + "?",
                                "¿Descartar?",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (resp == JOptionPane.YES_OPTION) {
                            controller.descartarAlerta(reserva);
                        }
                    }
                }
            }
        });
    }


    public JPanel getPanel1() {
        return panel1;
    }

    public void setPanel1(JPanel panel1) {
        this.panel1 = panel1;
    }

    public AlertasModel getModel() {
        return model;
    }

    public void setModel(AlertasModel model) {
        this.model = model;
    }

    public AlertasController getController() {
        return controller;
    }

    public void setController(AlertasController controller) {
        this.controller = controller;
    }

    public JTable getAlertas() {
        return alertas;
    }

    public void setAlertas(JTable alertas) {
        this.alertas = alertas;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (AlertasModel.RESERVAS.equals(evt.getPropertyName())) {
            int[] cols = {AlertasTableModel.ACTIVIDAD,
                    AlertasTableModel.VER,
                    AlertasTableModel.DESCRIPCION};
            alertas.setModel(new AlertasTableModel(cols, model.getList()));
        }
        this.panel1.revalidate();
        this.panel1.repaint();
    }
}
