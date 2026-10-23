package IIProyectoP3.logic;

import domain.Protocol;
import domain.Reservation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server {
    ServerSocket srv;
    List<Worker> workers;

    public Server() {
        try {
            srv = new ServerSocket(Protocol.PORT);
            workers = new ArrayList<Worker>();
            System.out.println("Servidor iniciado...");
        } catch (IOException ex) { System.out.println(ex);}
    }

    public void run() {
        boolean continuar = true;
        Socket s;
        Worker worker;
        while (continuar) {
            try {
                s = srv.accept();
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                os.flush();
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());

                int type = is.readInt();
                if (type == Protocol.SYNC){
                    String sid = UUID.randomUUID().toString();
                    os.writeObject(sid);
                    os.flush();
                    System.out.println("Conexion Establecida..." + s.getInetAddress() + ":" + s.getPort());
                    worker = new Worker(this, s, os, is, sid);
                    this.add(worker);
                    worker.start();
                } else if (type == Protocol.ASYNC){
                    String sid = (String) is.readObject();
                    Worker w = find(sid);
                    if (w != null){
                        w.setAs(s,os);
                        System.out.println("Canal de alertas conectado");
                    } else{
                        s.close();
                    }
                }
            } catch (Exception ex) { System.out.println(ex);}
        }
    }

    public void add(Worker w) {
        synchronized(workers) {
            workers.add(w);
        }
        System.out.println("Quedan: " + workers.size());
    }

    public void remove(Worker w) {
        synchronized(workers) {
            workers.remove(w);
            System.out.println("Quedan: " + workers.size());
        }
    }

    private Worker find(String sid){
        synchronized (workers){
            for (Worker w : workers){
                if (w.sid.equals(sid)) return w;
            }
        }
        return null;
    }

    public void deliverReservationAlert(Reservation r, Worker sender) {
        synchronized(workers) {
            System.out.println("Enviando alerta " + r.getId() + " a " + (workers.size() - 1) + " cliente(s)");
            for (Worker w : workers) {
                if (w != sender) w.deliver(r);
            }
        }
    }

}
