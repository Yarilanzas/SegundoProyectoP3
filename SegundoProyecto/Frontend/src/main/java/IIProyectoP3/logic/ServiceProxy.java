package IIProyectoP3.logic;


import domain.IService;
import domain.Protocol;
import domain.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

    private ServiceProxy(){
        try{
            sS=new Socket(Protocol.SERVER, Protocol.PORT); //conexion por red al servidor
            os= new ObjectOutputStream(sS.getOutputStream());//camino para enviar solicitudes al backend
            is= new ObjectInputStream(sS.getInputStream());//camino para recibir cosas del backend
            //envía un código inicial para identificarse o sincronizarse
            //el lo crea
            os.writeInt(Protocol.SYNC);
            os.flush();// fuerza a que el dato salga de una y que no se quede en el buffer
            sid= (String)is.readObject();// Lee la respuesta del servidor aca un id session


            //sockets:
            //síncrono el cliente pide algo y espera la response.
            //asíncrono El cliente no pide nada, solo está escuchando en segundo plano a que el servidor le avise de algún evento.

        }
        catch (Exception e){
            System.err.println("No se pudo conectar al Backend: " + e.getMessage());
            System.exit(-1);
        }

    }
    public String getSid() {
        return sid;
    }

    //Para login
    @Override
    public User login(String id, String password) throws Exception{
        os.writeInt(Protocol.Lo)

    }



}
