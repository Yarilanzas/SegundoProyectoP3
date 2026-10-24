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
        String sid;
        while (continuar) {
            try {
                s = srv.accept();
                System.out.println("Conexion Establecida...");
                ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                int type = is.readInt();
                switch (type) {
                    case Protocol.SYNC:
                        sid = s.getRemoteSocketAddress().toString();
                        System.out.println("SYNCH: " + sid);
                        worker = new Worker(this, s, os, is, sid);
                        this.add(worker);
                        worker.start();
                        os.writeObject(sid); // send Session id back
                        break;
                    case Protocol.ASYNC:
                        sid = (String) is.readObject(); // recieves Session Id
                        System.out.println("ASYNCH: " + sid);
                        join(s, os, is, sid);
                        break;
                }
                os.flush();
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex);
            }
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

    public void join(Socket as,ObjectOutputStream aos, ObjectInputStream ais, String sid){
        synchronized(workers) {
            for (Worker w : workers) {
                if (w.sid.equals(sid)) {
                    w.setAs(as, aos, ais);
                    break;
                }
            }
        }
    }

    public void deliver_reservation(Worker from, Reservation r){
        synchronized(workers) {
            for (Worker w : workers) {
                if (w != from) w.deliver_reservation(r);
            }
        }
    }

}
