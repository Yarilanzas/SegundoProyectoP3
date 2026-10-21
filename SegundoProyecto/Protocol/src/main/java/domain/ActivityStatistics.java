package domain;
import java.io.Serializable;



public class ActivityStatistics implements Serializable{
    private String semana;
    private int cantidad;

    public ActivityStatistics(String semana, int cantidad) {
        this.semana = semana;
        this.cantidad = cantidad;
    }

    public String getSemana() {
        return semana;
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
