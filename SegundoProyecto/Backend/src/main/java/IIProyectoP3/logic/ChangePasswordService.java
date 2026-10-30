package IIProyectoP3.logic;

import IIProyectoP3.data.UserDAO;
import domain.User;

import java.sql.SQLException;

public class ChangePasswordService {

    private final UserDAO userDAO;

    public ChangePasswordService(){
        this.userDAO = new UserDAO();
    }

    public void changePassword(String id, String actual, String nueva, String confirmacion) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("El ID de usuario no es válido");
        }
        if (actual == null || actual.isEmpty()
                || nueva == null || nueva.isEmpty()
                || confirmacion == null || confirmacion.isEmpty()) {
            throw new Exception("Todos los campos de contraseña son obligatorios");
        }
        if (!nueva.equals(confirmacion)) {
            throw new Exception("La contraseña nueva y la confirmación no coinciden");
        }

        User userActualizado = null;

        try {
            userActualizado = userDAO.read(id.trim());
        } catch (SQLException e) {
            throw new Exception("Error de conexión con la base de datos: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Usuario no encontrado");
        }

        if (!userActualizado.getPassword().equals(actual)) {
            throw new Exception("La contraseña actual es incorrecta");
        }

        userDAO.changePassword(userActualizado.getId(), nueva);
    }
}