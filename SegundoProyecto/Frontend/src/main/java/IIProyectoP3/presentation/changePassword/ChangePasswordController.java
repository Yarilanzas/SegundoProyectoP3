package IIProyectoP3.presentation.changePassword;


import IIProyectoP3.logic.ChangePasswordService;
import IIProyectoP3.logic.ServiceProxy;
import domain.IService;

public class ChangePasswordController {
    private ChangePasswordView view;
    private ChangePasswordModel model;
    private ServiceProxy serviceProxy;
    //private final ChangePasswordService service = new ChangePasswordService();
    private String currentUserId; // Guarda el ID del usuario que tiene la sesión abierta

    public ChangePasswordController(ChangePasswordView view, ChangePasswordModel model, String currentUserId, ServiceProxy serviceProxy) {
        this.view = view;
        this.model = model;
        this.serviceProxy = serviceProxy;
        this.currentUserId = currentUserId;

        this.view.setController(this);
        this.view.setModel(model);
    }

    public void change(String actual, String nueva, String confirmacion) throws Exception {
        serviceProxy.changePassword(actual, nueva, confirmacion);
    }
}