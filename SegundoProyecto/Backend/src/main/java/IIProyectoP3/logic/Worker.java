package IIProyectoP3.logic;

import domain.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class Worker {
    Server srv;
    Socket s;
    ObjectOutputStream os;
    ObjectInputStream is;

    String sid;
    Socket as;
    ObjectOutputStream aos;
    ObjectInputStream ais;
    User user;

    LoginService loginService = LoginService.instance();
    ChangePasswordService changePasswordService = new ChangePasswordService();
    EmployeeService employeeService = new EmployeeService();
    CategoryService categoryService = new CategoryService();
    ResourceService resourceService = new ResourceService();
    ReservationService reservationService = new ReservationService();
    ReservationQueryService reservationQueryService = new ReservationQueryService();

    public Worker(Server srv, Socket s, ObjectOutputStream os, ObjectInputStream is, String sid) {
        this.srv = srv;
        this.s = s;
        this.os = os;
        this.is = is;
        this.sid = sid;
    }

    public void setAs(Socket as, ObjectOutputStream aos, ObjectInputStream ais){
        this.as = as;
        this.aos = aos;
        this.ais = ais;
    }

    boolean continuar;
    public void start(){
        System.out.println("Worker atendiendo peticiones...");
        Thread t = new Thread(new Runnable(){
            public void run(){
               listen();
            }
        });
        continuar = true;
        t.start();
    }

    public void stop(){
        continuar = false;
        try { s.close();} catch (Exception ignored) {}
        try {if (as != null) as.close();} catch (Exception ignored) {}
        srv.remove(this);
        System.out.println("Conexion cerrada...");
    }

    public void listen(){
        int method;
        while (continuar) {
            try {
                method = is.readInt();
                System.out.println("Operacion: " + method);
                synchronized (Server.class) {
                    switch (method) {

                        //login
                        case Protocol.USER_LOGIN_READ:
                            try {
                                String id = (String) is.readObject();
                                String pass = (String) is.readObject();
                                user = loginService.read(id, pass);
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, user, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.LOGOUT:
                            user = null;
                            os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            break;
                        case Protocol.USER_CHANGE_PASSWORD:
                            try {
                                String actual = (String) is.readObject();
                                String nueva = (String) is.readObject();
                                String confirmacion = (String) is.readObject();
                                if (user == null) throw new Exception("Debe iniciar sesión");
                                changePasswordService.changePassword(user.getId(), actual, nueva, confirmacion);
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;

                        //employee
                        case Protocol.EMPLOYEE_FIND_ALL:
                            try {
                                List<Employee> le = employeeService.findAll();
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, le, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.EMPLOYEE_FIND_BY_ID:
                            try {
                                Employee e = employeeService.findById((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, e, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.EMPLOYEE_FIND_BY_NAME:
                            try {
                                List<Employee> le = employeeService.findByName((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, le, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.EMPLOYEE_SAVE:
                            try {
                                employeeService.save((Employee) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.EMPLOYEE_DELETE:
                            try {
                                employeeService.delete((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;

                        //category
                        case Protocol.CATEGORY_FIND_ALL:
                            try {
                                List<Category> lc = categoryService.findAll();
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, lc, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.CATEGORY_FIND_BY_ID:
                            try {
                                Category c = categoryService.findById((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, c, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.CATEGORY_FIND_BY_DESC:
                            try {
                                Category c = categoryService.findByDesc((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, c, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.CATEGORY_SAVE:
                            try {
                                categoryService.save((Category) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.CATEGORY_DELETE:
                            try {
                                categoryService.delete((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;

                        //resources
                        case Protocol.RESOURCE_FIND_ALL:
                            try {
                                List<Resource> lr = resourceService.findAllResources();
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, lr, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESOURCE_FIND_BY_DESC:
                            try {
                                List<Resource> lr = resourceService.findByDesc((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, lr, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESOURCE_FIND_BY_CATEGORY:
                            try {
                                List<Resource> lr = resourceService.findByCategory((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, lr, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESOURCE_SAVE:
                            try {
                                resourceService.save((Resource) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESOURCE_UPDATE:
                            try {
                                resourceService.update((Resource) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESOURCE_DELETE:
                            try {
                                resourceService.delete((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;

                        //reservations
                        case Protocol.RESERVATION_SAVE:
                            try {
                                Reservation r = (Reservation) is.readObject();
                                List<Category> cats = (List<Category>) is.readObject();
                                reservationService.save(r, cats);
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, r, null));
                                os.flush();
                                srv.deliver_reservation(this, r);   // alerta
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESERVATION_FIND_ALL:
                            try {
                                List<Reservation> lr = reservationService.findAllResources();
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, lr, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESERVATION_EXTRAER:
                            try {
                                ReservaExtraccion re = reservationService.extraerReserva((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, re, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESERVATION_CANCEL:
                            try {
                                reservationService.cancel((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESERVATION_DELETE:
                            try {
                                reservationService.delete((String) is.readObject());
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, null, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;

                        //query
                        case Protocol.RESERVATION_QUERY_GET_CALENDAR:
                            try {
                                LocalDate fecha = (LocalDate) is.readObject();
                                Category cat = (Category) is.readObject();
                                CalendarData cd = reservationQueryService.getCalendar(fecha, cat);
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, cd, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESERVATION_QUERY_GET_HORARIO_SEMANAL:
                            try {
                                LocalDate fecha = (LocalDate) is.readObject();
                                ActivityData ad = reservationQueryService.getHorarioSemanal(fecha);
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, ad, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESERVATION_QUERY_GET_RESOURCE_STATS:
                            try {
                                LocalDate desde = (LocalDate) is.readObject();
                                LocalDate hasta = (LocalDate) is.readObject();
                                List<CategoryStatistics> ls = reservationQueryService.getResourceStatistics(desde, hasta);
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, ls, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;
                        case Protocol.RESERVATION_QUERY_GET_ACTIVITY_STATS:
                            try {
                                LocalDate desde = (LocalDate) is.readObject();
                                LocalDate hasta = (LocalDate) is.readObject();
                                List<ActivityStatistics> ls = reservationQueryService.getActivityStatistics(desde, hasta);
                                os.writeObject(new Response(Protocol.ERROR_NO_ERROR, ls, null));
                            } catch (Exception ex) {
                                os.writeObject(new Response(Protocol.ERROR_ERROR, null, ex.getMessage()));
                            }
                            break;

                        // desconectar
                        case Protocol.DISCONNECT:
                            stop();
                            srv.remove(this);
                            break;
                    }
                }
                if (continuar) os.flush();
            } catch (Exception e) {
                stop();
                srv.remove(this);
            }
        }
    }

    public synchronized void deliver_reservation(Reservation r){
        if (aos == null || user == null) return;
        try {
            aos.writeInt(Protocol.DELIVER_RESERVATION);
            aos.writeObject(r);
            aos.flush();
        } catch (Exception e){
            System.out.println("No se pudo enviar la alerta: " + e.getMessage());
        }

    }

}
