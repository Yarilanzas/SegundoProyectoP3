package IIProyectoP3.presentation.alertas;

import IIProyectoP3.logic.ServiceProxy;
import IIProyectoP3.presentation.SocketListener;
import IIProyectoP3.presentation.SocketMonitor;
import domain.Reservation;
import domain.Resource;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class AlertasController implements SocketListener {
    AlertasView view;
    AlertasModel model;
    SocketMonitor monitor;

    public AlertasController(AlertasView view, AlertasModel model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        this.model.addPropertyChangeListener(view);

        try{
            monitor = new SocketMonitor(this, ((ServiceProxy)ServiceProxy.instance()).getSid());
            monitor.start();
        } catch (Exception e) {}



    }



   @Override
    public void deliver_reservation(Reservation r) {
        List<Reservation> reservaciones = new ArrayList<>(model.getList());
        reservaciones.add(0, r);
        model.setList(reservaciones);
      // System.out.println("Alerta recibida: ");
    }


    public void descartarAlerta(Reservation r) {
        List<Reservation> reservaciones = new ArrayList<>(model.getList());
        reservaciones.remove(r);
        model.setList(reservaciones);
    }

    public void stop() {
        monitor.stop();
    }

    public void mostrarDetalleReserva(Reservation reserva){
        buildMostrarWindow(reserva);
    }

    private void buildDescartarWindow() {
        JFrame DescartarFrame = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane();
        DescartarView descView = new DescartarView();
        tabbedPane.addTab("Descartar?", descView.getPanel());

    }

    private void buildMostrarWindow(Reservation reserva) {
        JFrame MostrarFrame = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane();
        MostrarReservaView mostrarView = new MostrarReservaView();
        tabbedPane.addTab(reserva.getActivity(), mostrarView.getPanel());

        mostrarView.getLabelEncargado().setText(reserva.getEmployee().getName());
        mostrarView.getFechaLabel().setText(reserva.getDate().toString());
        mostrarView.getDesdeLabel().setText(reserva.getStartTime());
        mostrarView.getHastaLabel().setText(reserva.getEndTime());


        DefaultListModel<String> model = (DefaultListModel<String>) mostrarView.getListaRecursos().getModel();

        if (model == null) {
            model = new DefaultListModel<>();
            mostrarView.getListaRecursos().setModel(model);
        }

        model.clear();

        if (reserva.getResources() != null) {
            for (Resource r : reserva.getResources()) {
                model.addElement(r.getId() + " - " + r.getDescription());
            }
        }

        MostrarFrame.add(tabbedPane);
        MostrarFrame.pack();
        MostrarFrame.setLocationRelativeTo(null);
        MostrarFrame.setVisible(true);

    }
}
