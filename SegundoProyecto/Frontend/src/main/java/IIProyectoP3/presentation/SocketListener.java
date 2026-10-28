package IIProyectoP3.presentation;

import domain.Reservation;

public interface SocketListener {
    void deliver_reservation(Reservation reservation);
}