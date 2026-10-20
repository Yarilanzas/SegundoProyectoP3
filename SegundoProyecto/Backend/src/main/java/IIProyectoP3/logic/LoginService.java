package IIProyectoP3.logic;

import IIProyectoP3.data.UserDAO;
import domain.User;

public class LoginService {

    private static LoginService theInstance;
    private final UserDAO userDAO;

    public static LoginService instance() {
        if (theInstance == null) theInstance = new LoginService();
        return theInstance;
    }

    private LoginService() { this.userDAO = new UserDAO();
    }

    public User read(String id, String password) throws Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("EL ESPACIO DEL ID DEBE DE ESTAR COMPLETO");
        }

        String idClean = id.trim();
        String idUpper = idClean.toUpperCase();

        if (!idUpper.startsWith("ADM") && !idUpper.startsWith("FUN")){
            throw new Exception("El id debe iniciar con su distintivo (ADM o FUN)");
        }

        User result = null;
        try {
            result = userDAO.read(idClean);
        } catch (Exception e){

            System.err.println("--- ERROR REAL DE CONEXIÓN/SQL ---");
            e.printStackTrace();

            if (idUpper.startsWith("ADM")) {
                throw new Exception("Administrador no existe");
            } else {
                throw new Exception("Funcionario no existe");
            }
        }

        if (result == null) {
            if (idUpper.startsWith("ADM")) {
                throw new Exception("Administrador no existe");
            } else {
                throw new Exception("Funcionario no existe");
            }
        }

        if (!result.getPassword().equals(password)) {
            throw new Exception("Contraseña incorrecta");
        }

        return result;
    }
}


