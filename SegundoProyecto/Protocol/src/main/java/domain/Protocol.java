package domain;

public class Protocol {
    public static final String SERVER = "localhost";
    public static final int PORT = 1234;

    //Login
    public static final int USER_LOGIN_READ=100;
    public static final int LOGOUT=101;
    public static final int USER_CHANGE_PASSWORD=102;

    public static final int SYNC = 103;    // primera conexión: canal de peticiones
    public static final int ASYNC = 104;   // segunda conexión: canal de alertas
    public static final int DELIVER_RESERVATION = 901;  // alerta servidor al cliente


    //Users, employee
    public static final int EMPLOYEE_FIND_ALL = 201;
    public static final int EMPLOYEE_FIND_BY_ID = 202;
    public static final int EMPLOYEE_FIND_BY_NAME = 203;
    public static final int EMPLOYEE_SAVE = 204;
    public static final int EMPLOYEE_DELETE = 205;

    //Resources
    public static final int RESOURCE_FIND_ALL=301;
    public static final int RESOURCE_FIND_BY_DESC=302;
    public static final int RESOURCE_FIND_BY_CATEGORY=303;
    public static final int RESOURCE_UPDATE=304;
    public static final int RESOURCE_DELETE=305;
    public static final int RESOURCE_SAVE=306;

    //Reservation
    public static final int RESERVATION_SAVE = 401;
    public static final int RESERVATION_FIND_ALL = 402;
    public static final int RESERVATION_EXTRAER = 403;
    public static final int RESERVATION_DELETE = 404;
    public static final int RESERVATION_CANCEL = 405;

    //Category
    public static final int CATEGORY_FIND_ALL = 501;
    public static final int CATEGORY_FIND_BY_ID = 502;
    public static final int CATEGORY_FIND_BY_DESC=503;
    public static final int CATEGORY_DELETE=504;
    public static final int CATEGORY_SAVE=505;

    //Reservation Query
    public static final int RESERVATION_QUERY_GET_CALENDAR = 601;
    public static final int RESERVATION_QUERY_GET_HORARIO_SEMANAL = 602;
    public static final int RESERVATION_QUERY_GET_RESOURCE_STATS = 603;
    public static final int RESERVATION_QUERY_GET_ACTIVITY_STATS = 604;

    //Estados de respuesta
    public static final int ERROR_NO_ERROR=0;
    public static final int ERROR_ERROR=1;

    public static final int DISCONNECT=99;


}
