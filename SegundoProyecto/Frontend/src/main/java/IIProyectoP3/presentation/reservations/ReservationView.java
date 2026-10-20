package IIProyectoP3.presentation.reservations;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import domain.*;
import IIProyectoP3.presentation.category.CategoryTableModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationView implements PropertyChangeListener {
    private JPanel Panel;
    private JTextField frasetextField;
    private JTextField ActividadtextField1;
    private JButton extraerButton;
    private JPanel panel;
    private JPanel categoriasPanel;
    private JButton reservarButton;
    private JButton cancelarReservaSeleccionadaButton;
    private JButton limpiarButton;
    private JTable reservastable1;
    private JButton imprimirButton1;
    private JPanel PrincipalPanel;
    private DatePicker datePicker;
    private TimePicker horaInicio;
    private TimePicker horaFin;
    private JTable categoriastable2;

    private List<Category> categoriasSeleccionadas = new ArrayList<>();

    private ReservationModel model;
    private ReservationController controller;

    public ReservationView() {

        categoriastable2.setRowHeight(35);
        categoriastable2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        reservastable1.setRowHeight(35);
        reservastable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        categoriastable2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                categoriasSeleccionadas.clear();
                frasetextField.setText("");
                categoriastable2.clearSelection();
                if (controller != null) {
                    controller.clear();
                }
            }
        });

        categoriastable2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                categoriasSeleccionadas.clear();
                int[] selectedRows = categoriastable2.getSelectedRows();

                if (selectedRows.length > 0 && model != null && model.getCategories() != null) {
                    for (int row : selectedRows) {
                        Category cat = model.getCategories().get(row);
                        categoriasSeleccionadas.add(cat);
                    }
                }
            }
        });

        reservarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Reservation nuevaReserva = takeReservation();

                    controller.saveReservation(nuevaReserva, categoriasSeleccionadas);

                    JOptionPane.showMessageDialog(PrincipalPanel, "Reserva creada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PrincipalPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelarReservaSeleccionadaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reservastable1.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(PrincipalPanel, "Debe seleccionar una reserva de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Reservation reserva = model.getReservations().get(selectedRow);
                String idReserva = reserva.getId();

                int confirm = JOptionPane.showConfirmDialog(
                        PrincipalPanel,
                        "¿Seguro/a que desea cancelar la reserva " + idReserva + "?",
                        "Confirmar Cancelación",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        controller.cancelReservation(idReserva);
                        JOptionPane.showMessageDialog(PrincipalPanel, "Reserva cancelada y recursos liberados con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(PrincipalPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        imprimirButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    controller.print();
                }catch (Exception ex) {
                    JOptionPane.showMessageDialog(PrincipalPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        extraerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String frase = frasetextField.getText();
                    if (frase == null || frase.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(PrincipalPanel, "Ingrese una frase para extraer la información.", "Atención", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    ReservaExtraccion datos = controller.extraerDeFrase(frase);

                    if (datos != null) {
                        // Rellenar campos de la interfaz gráfica automáticamente
                        if (datos.getActividad() != null) {
                            ActividadtextField1.setText(datos.getActividad());
                        }

                        if (datos.getFecha() != null) {
                            datePicker.setDate(java.time.LocalDate.parse(datos.getFecha()));
                        }

                        if (datos.getHoraInicio() != null) {
                            horaInicio.setTime(LocalTime.parse(datos.getHoraInicio()));
                        }

                        if (datos.getHoraFinal() != null) {
                            horaFin.setTime(LocalTime.parse(datos.getHoraFinal()));
                        }

                        // Seleccionar las categorías extraídas en la JTable
                        if (datos.getCategoriasRecurso() != null && !datos.getCategoriasRecurso().isEmpty()) {
                            categoriastable2.clearSelection();
                            categoriasSeleccionadas.clear();

                            List<Category> categoriasSistema = model.getCategories();
                            for (int i = 0; i < categoriasSistema.size(); i++) {
                                Category cat = categoriasSistema.get(i);
                                if (datos.getCategoriasRecurso().contains(cat.getDescription())) {
                                    categoriastable2.addRowSelectionInterval(i, i);
                                    categoriasSeleccionadas.add(cat);
                                }
                            }
                        }

                        JOptionPane.showMessageDialog(PrincipalPanel, "Datos extraídos exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PrincipalPanel, "Error al extraer datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public JPanel getPanel() {
        return PrincipalPanel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public ReservationController getController() {
        return controller;
    }

    public void setController(ReservationController controller) {
        this.controller = controller;
    }

    public ReservationModel getModel() {
        return model;
    }

    private String validateTextField(JTextField field, String fieldName) throws Exception {
        String text = field.getText() != null ? field.getText().trim() : "";
        if (text.isEmpty()) {
            field.requestFocus();
            throw new Exception("El campo '" + fieldName + "' es obligatorio.");
        }
        return text;
    }

    private void createUIComponents() {
        categoriastable2 = new JTable();
        datePicker = new DatePicker();
        horaInicio = new TimePicker();
        horaFin = new TimePicker();
    }

    public void setModel(ReservationModel model) {
        this.model = model;
    }

    private ImageIcon getIcono(String ruta) {
        URL url = getClass().getResource(ruta);
        return (url != null) ? new ImageIcon(url) : null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ReservationModel.CURRENT:
                Reservation r = model.getCurrent();
                ActividadtextField1.setText(r.getActivity() == null ? "" : r.getActivity());
                datePicker.setDate(r.getDate());

                if (r.getStartTime() != null && !r.getStartTime().isEmpty()) {
                    horaInicio.setTime(LocalTime.parse(r.getStartTime()));
                } else {
                    horaInicio.setTime(null);
                }

                if (r.getEndTime() != null && !r.getEndTime().isEmpty()) {
                    horaFin.setTime(LocalTime.parse(r.getEndTime()));
                } else {
                    horaFin.setTime(null);
                }
                categoriastable2.clearSelection();
                break;

            case ReservationModel.CATEGORIES:
                int[] colsCat = {CategoryTableModel.ID, CategoryTableModel.DESCRIPCION};
                categoriastable2.setModel(new CategoryTableModel(colsCat, model.getCategories()));
                break;

            case ReservationModel.RESERVATIONS:
                int[] colum = {
                        ReservationTableModel.ID,
                        ReservationTableModel.ACTIVIDAD,
                        ReservationTableModel.FECHA,
                        ReservationTableModel.HORARIO,
                        ReservationTableModel.RECURSO,
                        ReservationTableModel.ESTADO
                };
                reservastable1.setModel(new ReservationTableModel(colum, model.getReservations()));
                break;
        }
        this.PrincipalPanel.revalidate();
    }

    public Reservation takeReservation() throws Exception {
        Reservation r = new Reservation();

        String actividad = validateTextField(ActividadtextField1, "Actividad");
        r.setActivity(actividad);

        if (datePicker.getDate() == null) {
            datePicker.requestFocus();
            throw new Exception("Debe seleccionar una fecha para la reservación.");
        }
        r.setDate(datePicker.getDate());

        if (horaInicio.getTime() == null) {
            horaInicio.requestFocus();
            throw new Exception("Debe seleccionar la hora de inicio.");
        }
        r.setStartTime(horaInicio.getTime().toString());

        if (horaFin.getTime() == null) {
            horaFin.requestFocus();
            throw new Exception("Debe seleccionar la hora de finalización.");
        }
        r.setEndTime(horaFin.getTime().toString());

        if (categoriasSeleccionadas.isEmpty()) {
            throw new Exception("Debe seleccionar al menos una categoría de la tabla.");
        }
        if (UserSession.getInstance().isLoggedIn()) {
            User usuario = UserSession.getInstance().getCurrentUser();
            if (usuario instanceof Employee) {
                r.setEmployee((Employee) usuario);
            } else {
                throw new Exception("El usuario en sesión no es un funcionario/empleado válido para realizar reservas.");
            }
        } else {
            throw new Exception("No hay ninguna sesión de usuario activa. Debe iniciar sesión.");
        }

        return r;
    }
}