package IIProyectoP3.presentation.statistics;

import com.github.lgooddatepicker.components.DatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import domain.CategoryStatistics;
import domain.ActivityStatistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;


public class StatisticsView implements PropertyChangeListener{
    private JButton cargarButtonA;
    private JTable tableActividades;
    private JPanel panel;
    private JPanel principalPanel;
    private DatePicker desdeRecursos;
    private DatePicker hastaRecursos;
    private JButton cargarButton;
    private DatePicker desdeActividades;
    private DatePicker hastaActividades;
    private JTable tableCategorias;
    private JPanel graficoRecursos;
    private JPanel graficoActividades;

    private StatisticsModel model;
    private StatisticsController controller;

    public StatisticsView() {

        tableActividades.setRowHeight(35);
        tableActividades.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tableCategorias.setRowHeight(35);
        tableCategorias.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        cargarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate desde = desdeRecursos.getDate();
                LocalDate hasta = hastaRecursos.getDate();
                if (desde  == null || hasta == null){
                    JOptionPane.showMessageDialog(principalPanel,"Seleccionar ambas fechas");
                    return;
                }
                controller.cargarRecursos(desde,hasta);
            }
        });
        cargarButtonA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate desde = desdeActividades.getDate();
                LocalDate hasta = hastaActividades.getDate();
                if (desde  == null || hasta == null){
                    JOptionPane.showMessageDialog(principalPanel,"Seleccionar ambas fechas");
                    return;
                }
                controller.cargarActividades(desde,hasta);
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        switch (evt.getPropertyName()){
            case StatisticsModel.CATEGORIES:
                int [] colsCate = {CategoriesTableModel.CATEGORIA,CategoriesTableModel.CANTIDAD};
                tableCategorias.setModel(new CategoriesTableModel(colsCate,model.getCategoryStats()));
                mostrargraficoRecursos();
                break;
            case StatisticsModel.ACTIVITIES:
                int [] colsAct = {ActivitiesTableModel.SEMANA,ActivitiesTableModel.CANTIDAD};
                tableActividades.setModel(new ActivitiesTableModel(colsAct,model.getActivityStats()));
                mostrargraficoActividades();
                break;
        }
    }

    public void mostrargraficoRecursos(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (CategoryStatistics ce : model.getCategoryStats()){
            dataset.addValue(ce.getCantidad(),"Recurso",ce.getCategory().getDescription());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Recursos usados","Categoria", "Cantidad",
                dataset,PlotOrientation.VERTICAL,true,true,false);
        ChartPanel chartPanel = new ChartPanel(chart);
        graficoRecursos.removeAll();
        graficoRecursos.setLayout(new BorderLayout());
        graficoRecursos.add(chartPanel, BorderLayout.CENTER);
        graficoRecursos.revalidate();
        graficoRecursos.repaint();
    }

    public void mostrargraficoActividades(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (ActivityStatistics ae : model.getActivityStats()){
            dataset.addValue(ae.getCantidad(),"Semana", ae.getSemana());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Actividades realizadas","Semana", "Cantidad",
                dataset,PlotOrientation.VERTICAL,true,true,false);
        ChartPanel chartPanel = new ChartPanel(chart);
        graficoActividades.removeAll();
        graficoActividades.setLayout(new BorderLayout());
        graficoActividades.add(chartPanel, BorderLayout.CENTER);
        graficoActividades.revalidate();
        graficoActividades.repaint();
    }

    public JPanel getPanel() {
        return principalPanel;
    }

    public void setPanel(JPanel panel) {
        this.principalPanel = panel;
    }

    public StatisticsController getController() {
        return controller;
    }

    public void setController(StatisticsController controller) {
        this.controller = controller;
    }

    public StatisticsModel getModel() {
        return model;
    }

    public void setModel(StatisticsModel model) {
        this.model = model;
        if (this.model != null){
            this.model.addPropertyChangeListener(this);
        }
    }
}
