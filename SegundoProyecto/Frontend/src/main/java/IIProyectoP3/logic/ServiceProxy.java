package IIProyectoP3.logic;


import domain.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class ServiceProxy implements IService {
    private static ServiceProxy theInstance;

    public static IService instance() {
        if (theInstance == null) theInstance = new ServiceProxy();
        return theInstance;
    }

    Socket sS; //crea el socket sincrono
    ObjectOutputStream os; //camino para enviar solicitudes al backend
    ObjectInputStream is; //camino para recibir cosas del backend
    String sid; //sessiom id lo que se guarda para saber que usuario esta interactuando

    private ServiceProxy() {
        try {
            sS = new Socket(Protocol.SERVER, Protocol.PORT); //conexion por red al servidor
            os = new ObjectOutputStream(sS.getOutputStream());//camino para enviar solicitudes al backend
            is = new ObjectInputStream(sS.getInputStream());//camino para recibir cosas del backend
            //envía un código inicial para identificarse o sincronizarse
            //el lo crea
            os.writeInt(Protocol.SYNC);
            os.flush();// fuerza a que el dato salga de una y que no se quede en el buffer
            sid = (String) is.readObject();// Lee la respuesta del servidor aca un id session


            //sockets:
            //síncrono el cliente pide algo y espera la response.
            //asíncrono El cliente no pide nada, solo está escuchando en segundo plano a que el servidor le avise de algún evento.

        } catch (Exception e) {
            System.err.println("No se pudo conectar al Backend: " + e.getMessage());
            System.exit(-1);
        }

    }

    public String getSid() {
        return sid;
    }

    //Para login
    @Override
    public User login(String id, String password) throws Exception {
        os.writeInt(Protocol.USER_LOGIN_READ);
        //argumentos
        os.writeObject(id);
        os.writeObject(password);
        os.flush();

        Response response = (Response) is.readObject();

        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (User) response.getData();
        } else {
            throw new Exception("No se pudo iniciar sesion");
        }
    }


    @Override
    public void changePassword(String actual, String nueva, String confirmacion) throws Exception {
        //parametrod
        os.writeInt(Protocol.USER_CHANGE_PASSWORD);//manda el numero etiqueta prestablecido para saber qu ehay que cambiar la contraseña
        os.writeObject(actual);
        os.writeObject(nueva);
        os.writeObject(confirmacion);
        os.flush();

        //os.writeObject: este metodo mete en una cola la info necesaria para completar la acción que se pidio con el int
        //convierte lo que le mandamos a bytes para que viaje por el socket

        // Respuesta del backend
        Response response = (Response) is.readObject();

        //ver si salio bien o mal
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {

        } else {
            throw new Exception("No se pudo cambiar la contraseña ");
        }
    }

    @Override
    public void logout() throws Exception {
        try {
            os.writeInt(Protocol.LOGOUT);
            os.flush();
            sS.close();
        } catch (Exception e) {
            System.err.println("No se pudo cerrar sesion");
        }
    }


    //Para employee
    @Override
    public List<Employee> employeeFindAll() throws Exception {
        os.writeInt(Protocol.EMPLOYEE_FIND_ALL);
        os.flush();

        Response response = (Response) is.readObject();

        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (List<Employee>) response.getData();
        } else {
            throw new Exception("No se pudieron cargar los empleados");
        }
    }

    public Employee employeeFindbyId(String id) throws Exception {
        os.writeInt(Protocol.EMPLOYEE_FIND_BY_ID);
        os.writeObject(id);
        os.flush();

        Response response = (Response) is.readObject();

        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (Employee) response.getData();
        } else {
            throw new Exception("No se encontró empleado");
        }
    }

    public List<Employee> employeeFindbyName(String name) throws Exception {


        os.writeInt(Protocol.EMPLOYEE_FIND_BY_NAME);
        os.writeObject(name);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (List<Employee>) response.getData();
        } else {
            throw new Exception("No se encontraron empleados con ese nombre");

        }

    }


    @Override
    public void employeeSave(Employee employee) throws Exception {
        os.writeInt(Protocol.EMPLOYEE_SAVE);
        os.writeObject(employee);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("No se pudo guardar el empleado");
        }
    }

    @Override
    public void employeeDelete(String id) throws Exception {
        os.writeInt(Protocol.EMPLOYEE_DELETE);
        os.writeObject(id);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("No se pudo eliminar el empleado");
        }
    }

//para category

    @Override
    public List<Category> categoryFindAll() throws Exception {
        os.writeInt(Protocol.CATEGORY_FIND_ALL);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (List<Category>) response.getData();
        } else {
            throw new Exception("No se encontraron categorías");
        }
    }

    @Override
    public Category categoryFindbyId(String id) throws Exception {
        os.writeInt(Protocol.CATEGORY_FIND_BY_ID);
        os.writeObject(id);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (Category) response.getData();
        } else {
            throw new Exception("No se encontró la categoría solicitada");
        }
    }

    @Override
    public List<Category> categoryFindbyDesc(String description) throws Exception {
        os.writeInt(Protocol.CATEGORY_FIND_BY_DESC);
        os.writeObject(description);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (List<Category>) response.getData();
        } else {
            throw new Exception("No se encontraron categorías con esa descripción");
        }
    }

    @Override
    public void categorySave(Category category) throws Exception {
        os.writeInt(Protocol.CATEGORY_SAVE);
        os.writeObject(category);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("No se pudo guardar la categoría");
        }
    }

    @Override
    public void categoryDelete(String id) throws Exception {
        os.writeInt(Protocol.CATEGORY_DELETE);
        os.writeObject(id);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("No se pudo eliminar la categoría");
        }
    }

    //para resources

    @Override
    public List<Resource> resourcesFindAll() throws Exception {
        os.writeInt(Protocol.RESOURCE_FIND_ALL);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (List<Resource>) response.getData();
        } else {
            throw new Exception("No se encontraron recursos");
        }
    }

    @Override
    public List<Resource> resourceFindbyDesc(String description) throws Exception {
        os.writeInt(Protocol.RESOURCE_FIND_BY_DESC);
        os.writeObject(description);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (List<Resource>) response.getData();
        } else {
            throw new Exception("No se encontraron recursos con esa descripción");
        }
    }

    @Override
    public List<Resource> resourceFindbyCategory(String category) throws Exception {
        os.writeInt(Protocol.RESOURCE_FIND_BY_CATEGORY);
        os.writeObject(category);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (List<Resource>) response.getData();
        } else {
            throw new Exception("No se encontraron recursos para esa categoría");
        }
    }

    @Override
    public void resourceSave(Resource resource) throws Exception {
        os.writeInt(Protocol.RESOURCE_SAVE);
        os.writeObject(resource);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("No se pudo guardar el recurso");
        }
    }

    @Override
    public void resourceUpdate(Resource resource) throws Exception {
        os.writeInt(Protocol.RESOURCE_UPDATE);
        os.writeObject(resource);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("No se pudo actualizar el recurso");
        }
    }

    @Override
    public void resourceDelete(String id) throws Exception {
        os.writeInt(Protocol.RESOURCE_DELETE);
        os.writeObject(id);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("No se pudo eliminar el recurso");
        }
    }

    //para reservations

    @Override
    public Reservation reservationSave(Reservation reservation, List<Category> categories) throws Exception {
        os.writeInt(Protocol.RESERVATION_SAVE);
        os.writeObject(reservation);
        os.writeObject(categories);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (Reservation) response.getData();
        } else {
            throw new Exception("No se pudo realizar la reservación");
        }
    }

    @Override
    public List<Reservation> reservationFindAll() throws Exception {
        os.writeInt(Protocol.RESERVATION_FIND_ALL);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (List<Reservation>) response.getData();
        } else {
            throw new Exception("No se encontraron reservaciones");
        }
    }

    @Override
    public ReservaExtraccion reservationExtraer(String frase) throws Exception {
        os.writeInt(Protocol.RESERVATION_EXTRAER);
        os.writeObject(frase);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (ReservaExtraccion) response.getData();
        } else {
            throw new Exception("No se pudo extraer la información de la reservación");
        }
    }

    @Override
    public void reservationCancel(String id) throws Exception {
        os.writeInt(Protocol.RESERVATION_CANCEL);
        os.writeObject(id);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("No se pudo cancelar la reservación");
        }
    }

    @Override
    public void reservationDelete(String id) throws Exception {
        os.writeInt(Protocol.RESERVATION_DELETE);
        os.writeObject(id);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() != Protocol.ERROR_NO_ERROR) {
            throw new Exception("No se pudo eliminar la reservación");
        }
    }

    //para query

    @Override
    public CalendarData getCalendar(LocalDate fecha, Category category) throws Exception {
        os.writeInt(Protocol.RESERVATION_QUERY_GET_CALENDAR);
        os.writeObject(fecha);
        os.writeObject(category);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (CalendarData) response.getData();
        } else {
            throw new Exception("No se pudieron cargar los datos del calendario");
        }
    }

    @Override
    public ActivityData getHorarioSemanal(LocalDate fecharef) throws Exception {
        os.writeInt(Protocol.RESERVATION_QUERY_GET_HORARIO_SEMANAL);
        os.writeObject(fecharef);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (ActivityData) response.getData();
        } else {
            throw new Exception("No se pudieron obtener los horarios semanales");
        }
    }

    @Override
    public List<CategoryStatistics> getResourceStatistics(LocalDate desde, LocalDate hasta) throws Exception {
        os.writeInt(Protocol.RESERVATION_QUERY_GET_RESOURCE_STATS);
        os.writeObject(desde);
        os.writeObject(hasta);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (List<CategoryStatistics>) response.getData();
        } else {
            throw new Exception("No se pudieron obtener las estadísticas de recursos");
        }
    }

    @Override
    public List<ActivityStatistics> getActivityStatistics(LocalDate desde, LocalDate hasta) throws Exception {
        os.writeInt(Protocol.RESERVATION_QUERY_GET_ACTIVITY_STATS);
        os.writeObject(desde);
        os.writeObject(hasta);
        os.flush();

        Response response = (Response) is.readObject();
        if (response.getStatus() == Protocol.ERROR_NO_ERROR) {
            return (List<ActivityStatistics>) response.getData();
        } else {
            throw new Exception("No se pudieron obtener las estadísticas de actividad");
        }
    }

    ///

    @Override
    public void stop() {
        try {
            os.writeInt(Protocol.DISCONNECT);
            os.flush();
            if (sS != null && !sS.isClosed()) {
                sS.close();
            }
        } catch (Exception e) {
            System.err.println("Error al cerrar el Socket del cliente: " + e.getMessage());
        }
    }
}