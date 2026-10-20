package IIProyectoP3;
//import org.presentation.administrator.AdministratorView;
import IIProyectoP3.presentation.login.ControllerLogin;
import IIProyectoP3.presentation.login.Login;
import IIProyectoP3.presentation.login.ModelLogin;

import java.awt.*;


public class Main {
    public static void main(String[] args) {
        Login login = new Login();


        ModelLogin model = new ModelLogin();

        ControllerLogin controller = new ControllerLogin(login, model);

        login.pack();
        login.setLocationRelativeTo(null);
        login.setVisible(true);


        /*AdministratorView admin = new AdministratorView();
        JFrame frame = new JFrame("SISTEMA DE RESERVAS - PRUEBA");
        frame.setContentPane(admin.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }*/
    }
    public static final Color BACKGROUND_ERROR = new Color(255, 102, 102);
}
