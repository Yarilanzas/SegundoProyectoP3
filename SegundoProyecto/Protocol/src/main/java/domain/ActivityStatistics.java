package domain;


public class ActivityStatistics {
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
