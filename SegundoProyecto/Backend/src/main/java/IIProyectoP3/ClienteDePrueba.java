package IIProyectoP3;

import domain.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ClienteDePrueba {

    // ===== CAMBIAR SEGÚN TU BASE =====
    static final String USUARIO = "FUN-001";
    static final String CLAVE = "FUN-001";
    static final boolean CREAR_RESERVA = true;   // true para probar reserva + alerta

    public static void main(String[] args) throws Exception {

        // ---------- 1. Conexión de peticiones (SYNC) ----------
        Socket s = new Socket(Protocol.SERVER, Protocol.PORT);
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        os.flush();
        ObjectInputStream is = new ObjectInputStream(s.getInputStream());

        os.writeInt(Protocol.SYNC);
        os.flush();
        String sid = (String) is.readObject();
        System.out.println("Conectado. sid = " + sid);

        // ---------- 2. Login ----------
        os.writeInt(Protocol.USER_LOGIN_READ);
        os.writeObject(USUARIO);
        os.writeObject(CLAVE);
        os.flush();
        Response r = (Response) is.readObject();
        if (r.getStatus() == Protocol.ERROR_ERROR) {
            System.out.println("Login falló: " + r.getErrorMessage());
            s.close();
            return;
        }
        User user = (User) r.getData();
        System.out.println("Login OK: " + user.getId() + " (" + user.getClass().getSimpleName() + ")");

        // ---------- 3. Conexión de alertas (ASYNC) ----------
        Socket as = new Socket(Protocol.SERVER, Protocol.PORT);
        ObjectOutputStream aos = new ObjectOutputStream(as.getOutputStream());
        aos.flush();
        ObjectInputStream ais = new ObjectInputStream(as.getInputStream());
        aos.writeInt(Protocol.ASYNC);
        aos.writeObject(sid);
        aos.flush();

        Thread escuchador = new Thread(() -> {
            try {
                while (true) {
                    int op = ais.readInt();
                    if (op == Protocol.DELIVER_RESERVATION) {
                        Reservation alerta = (Reservation) ais.readObject();
                        System.out.println("\n>>> ALERTA RECIBIDA: " + alerta.getId() + " - "
                                + alerta.getActivity() + " (" + alerta.getDate() + " "
                                + alerta.getStartTime() + "-" + alerta.getEndTime() + ")");
                    }
                }
            } catch (Exception ex) {
                System.out.println("Canal de alertas cerrado: " + ex);
                ex.printStackTrace();
            }
        });
        escuchador.setDaemon(true);
        escuchador.start();
        System.out.println("Canal de alertas conectado.");

        // ---------- 4. Funcionarios ----------
        os.writeInt(Protocol.EMPLOYEE_FIND_ALL);
        os.flush();
        r = (Response) is.readObject();
        if (r.getStatus() == Protocol.ERROR_ERROR) {
            System.out.println("Error funcionarios: " + r.getErrorMessage());
        } else {
            List<Employee> empleados = (List<Employee>) r.getData();
            System.out.println("Funcionarios (" + empleados.size() + "):");
            for (Employee e : empleados) {
                System.out.println("   " + e.getId() + " - " + e.getName());
            }
        }

        // ---------- 5. Categorías ----------
        os.writeInt(Protocol.CATEGORY_FIND_ALL);
        os.flush();
        r = (Response) is.readObject();
        List<Category> categorias = null;
        if (r.getStatus() == Protocol.ERROR_ERROR) {
            System.out.println("Error categorías: " + r.getErrorMessage());
        } else {
            categorias = (List<Category>) r.getData();
            System.out.println("Categorías (" + categorias.size() + "):");
            for (Category c : categorias) {
                System.out.println("   " + c.getId() + " - " + c.getDescription());
            }
        }

        // ---------- 6. Reserva (opcional) ----------
        if (CREAR_RESERVA && categorias != null && !categorias.isEmpty()) {
            Reservation nueva = new Reservation();
            nueva.setActivity("Prueba desde consola");
            nueva.setDate(LocalDate.now().plusDays(1));
            nueva.setStartTime("08:00");
            nueva.setEndTime("09:00");
            nueva.setEmployee((Employee) user);

            os.writeInt(Protocol.RESERVATION_SAVE);
            os.writeObject(nueva);
            os.writeObject(List.of(categorias.get(0)));
            os.flush();
            r = (Response) is.readObject();
            if (r.getStatus() == Protocol.ERROR_ERROR) {
                System.out.println("Error reserva: " + r.getErrorMessage());
            } else {
                Reservation creada = (Reservation) r.getData();
                System.out.println("Reserva creada: " + creada.getId()
                        + " con " + creada.getResources().size() + " recurso(s)");
            }
        }

        // ---------- 7. Esperar alertas hasta Enter ----------
        System.out.println("\nEscuchando alertas... presioná Enter para salir.");
        new Scanner(System.in).nextLine();

        os.writeInt(Protocol.DISCONNECT);
        os.flush();
        s.close();
        as.close();
        System.out.println("Desconectado.");
    }
}
