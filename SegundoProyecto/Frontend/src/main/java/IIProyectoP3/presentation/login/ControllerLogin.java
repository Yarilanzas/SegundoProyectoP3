package IIProyectoP3.presentation.login;

import IIProyectoP3.logic.ServiceProxy;
import IIProyectoP3.presentation.alertas.AlertasController;
import IIProyectoP3.presentation.alertas.AlertasModel;
import IIProyectoP3.presentation.alertas.AlertasView;
import domain.IService;
import domain.User;
import IIProyectoP3.logic.LoginService;
import IIProyectoP3.presentation.activities.*;
import IIProyectoP3.presentation.category.*;
import IIProyectoP3.presentation.changePassword.ChangePasswordController;
import IIProyectoP3.presentation.changePassword.ChangePasswordModel;
import IIProyectoP3.presentation.changePassword.ChangePasswordView;
import IIProyectoP3.presentation.employees.*;
import IIProyectoP3.presentation.reservations.*;
import IIProyectoP3.presentation.resource.*;
import IIProyectoP3.presentation.scheduling.*;
import IIProyectoP3.presentation.statistics.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.awt.*;

public class ControllerLogin {
    private Login view;
    private ModelLogin model;
    //private IService serviceProxy;

    public ControllerLogin(Login view, ModelLogin model) {
        this.view = view;
        this.model = model;
        //this.serviceProxy = serviceProxy;

        view.setController(this);
        view.setModel(model);
    }

    public void login(String id, String password) {
        try {
           // User user = LoginService.instance().read(id, password);
            User user = ServiceProxy.instance().login(id, password);
            domain.UserSession.getInstance().login(user);

            model.setCurrent(user);

            String idUpper = user.getId().toUpperCase();
            if (idUpper.startsWith("ADM")) {
                buildAdminWindow();
            } else if (idUpper.startsWith("FUN")) {
                buildEmployeeWindow();
            }

            view.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void changePassword(String id) {
        JDialog frameChangePassword = new JDialog((Frame) null, "Cambiar Contraseña", true);

        ChangePasswordModel model = new ChangePasswordModel();
        ChangePasswordView view = new ChangePasswordView();
        ChangePasswordController controller = new ChangePasswordController(view, model, id);

        frameChangePassword.setContentPane(view.getPanel());

        frameChangePassword.setSize(400, 300);
        frameChangePassword.setLocationRelativeTo(null);
        frameChangePassword.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        frameChangePassword.setVisible(true);
    }




    private void buildAdminWindow() {
        JFrame adminFrame = new JFrame("Panel de Administración");
        JTabbedPane tabbedPane = new JTabbedPane();

        //funcionarios
        EmployeeModel empModel = new EmployeeModel();
        EmployeeView empView = new EmployeeView();
        EmployeeController empController = new EmployeeController(empModel, empView);
        ImageIcon iconEmp = new ImageIcon(getClass().getResource("/images/employee.png"));
        tabbedPane.addTab("Funcionarios", iconEmp, empView.getPanel());
        //tabbedPane.addTab("Funcionarios", empView.getPanel());
        //tabbedPane.setIconAt(1, new ImageIcon(getClass().getResource("/employee.png")));


        // Categorías
        CategoryModel catModel = new CategoryModel();
        CategoryView catView = new CategoryView();
        CategoryController catController = new CategoryController(catView, catModel);
        ImageIcon iconCat = new ImageIcon(getClass().getResource("/images/category1.png"));
        tabbedPane.addTab("Categorías", iconCat, catView.getPanel());

        // Recursos
        ResourceModel resModel = new ResourceModel();
        ResourceView resView = new ResourceView();
        ResourceController resController = new ResourceController(resView, resModel);
        ImageIcon iconRe = new ImageIcon(getClass().getResource("/images/resources.png"));
        tabbedPane.addTab("Recursos", iconRe, resView.getPanel());

        // Calendarización
        SchedulingModel schedModel = new SchedulingModel();
        SchedulingView schedView = new SchedulingView();
        SchedulingController schedController = new SchedulingController(schedView, schedModel);
        ImageIcon iconCal = new ImageIcon(getClass().getResource("/images/calendar.png"));
        tabbedPane.addTab("Calendarización", iconCal, schedView.getPanel());

        // Actividades
        ActivityModel actModel = new ActivityModel();
        ActivityView actView = new ActivityView();
        ActivityController actController = new ActivityController(actView, actModel);
        ImageIcon iconView = new ImageIcon(getClass().getResource("/images/activities.png"));
        tabbedPane.addTab("Actividades", iconView, actView.getPanel());

        // Estadísticas
        StatisticsModel statModel = new StatisticsModel();
        StatisticsView statView = new StatisticsView();
        StatisticsController statController = new StatisticsController(statView, statModel);
        ImageIcon iconStat = new ImageIcon(getClass().getResource("/images/statistic.png"));
        tabbedPane.addTab("Estadísticas", iconStat, statView.getPanel());

        //tabbedPane.addTab("Estadísticas", statView.getPanel());
        //tabbedPane.setIconAt(5, new ImageIcon(getClass().getResource("/statistic.png")));

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1 && tabbedPane.getTitleAt(selectedIndex).equals("Recursos")) {
                resController.cargarCategorias();
            }
        });

        // vista
        adminFrame.add(tabbedPane);
        adminFrame.setSize(900, 700);
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // adminFrame.setVisible(true);
        adminFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //se le dice a java que cuando el user aprete x no se cierre de golpe todo sino que se ejecute el listener de cierre primero
        adminFrame.addWindowListener(new java.awt.event.WindowAdapter() { // observador que ve todos los eventos de la ventana
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) { // este es el metodo que dice que hacer cuando se apreta x
                try {
                    ServiceProxy.instance().stop(); //llama al metofo stop de proxy que para llame al backend y pare las ejecucioens
                } catch (Exception ignored) {}
                System.exit(0);
            }
        });

        adminFrame.setVisible(true);
    }



    private void buildEmployeeWindow() {
        JFrame employeeFrame = new JFrame("Panel de Funcionario");
        JTabbedPane tabbedPane = new JTabbedPane();

        AlertasView alertasView = new AlertasView();
        AlertasModel alertasModel = new AlertasModel();
        AlertasController alertasController = new AlertasController(alertasView, alertasModel);

        JPanel alertasPanel = alertasView.getPanel1();
        alertasPanel.setBorder(BorderFactory.createTitledBorder("Alertas"));

        // Reservas
        ReservationModel resModel = new ReservationModel();
        ReservationView resView = new ReservationView();
        ReservationController resController = new ReservationController(resView, resModel);
        ImageIcon iconRe = new ImageIcon(getClass().getResource("/images/reservation1.png"));
        tabbedPane.addTab("Reservas", iconRe, resView.getPanel());

        // Calendarización
        SchedulingModel schedModel = new SchedulingModel();
        SchedulingView schedView = new SchedulingView();
        SchedulingController schedController = new SchedulingController(schedView, schedModel);
        ImageIcon iconCal = new ImageIcon(getClass().getResource("/images/calendar.png"));
        tabbedPane.addTab("Calendarización", iconCal, schedView.getPanel());

        //Actividades
        ActivityModel actModel = new ActivityModel();
        ActivityView actView = new ActivityView();
        ActivityController actController = new ActivityController(actView, actModel);
        ImageIcon iconView= new ImageIcon(getClass().getResource("/images/activities.png"));
        tabbedPane.addTab("Actividades", iconView, actView.getPanel());

        // Estadísticas
        StatisticsModel statModel = new StatisticsModel();
        StatisticsView statView = new StatisticsView();
        StatisticsController statController = new StatisticsController(statView, statModel);
        ImageIcon iconStat = new ImageIcon(getClass().getResource("/images/statistic.png"));
        tabbedPane.addTab("Estadísticas", iconStat, statView.getPanel());
       // tabbedPane.addTab("Estadísticas", statView.getPanel());

        // vista
        // 3. Aplicar la técnica del profesor (Layout null y setBounds)
        employeeFrame.setLayout(null);
        employeeFrame.add(tabbedPane);
        employeeFrame.add(alertasPanel);

        tabbedPane.setBounds(10, 10, 680, 630);
        alertasPanel.setBounds(700, 10, 170, 630);

        employeeFrame.setSize(900, 700);
        employeeFrame.setLocationRelativeTo(null);
        employeeFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        employeeFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    alertasController.stop();
                    ServiceProxy.instance().stop();
                } catch (Exception ignored) {}
                System.exit(0);
            }
        });

        employeeFrame.setVisible(true);
    }



}