/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IIProyectoP3.presentation;

import domain.Protocol;
import domain.Protocol;
import domain.Reservation;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketMonitor {
    SocketListener listener;
    String sid; // Session Id
    Socket as; // Asynchronous Socket
    ObjectOutputStream aos;
    ObjectInputStream ais;

    public SocketMonitor(SocketListener listener, String sid) throws Exception {
        this.listener = listener;
        as =new Socket(Protocol.SERVER, Protocol.PORT); //socket asincrono
        this.sid = sid;
        aos = new ObjectOutputStream(as.getOutputStream());
        ais = new ObjectInputStream(as.getInputStream());
        aos.writeInt(Protocol.ASYNC);
        aos.writeObject(sid);
        aos.flush();
    }

    boolean condition = true;
    private Thread t;
    public void start() {
        t = new Thread(new Runnable() {
            public void run() {
                listen();
            } });
        condition = true;
        t.start();
    }
    public void stop() { condition = false; }
    public void listen() {
        int method;
        while (condition) {
            try {
                method = ais.readInt();
                switch (method) {
                    case Protocol.DELIVER_RESERVATION:
                        try {
                            Reservation r = (Reservation) ais.readObject();
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    listener.deliver_reservation(r);} });
                        } catch (ClassNotFoundException ex) {}
                        break;
                }
            } catch (IOException ex) { condition = false; }
        }
        try {
            as.shutdownOutput();
            as.close();
        } catch (IOException e) {}
    }

}
