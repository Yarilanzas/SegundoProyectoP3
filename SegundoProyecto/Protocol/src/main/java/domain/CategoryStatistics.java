package domain;

public class CategoryStatistics {
    private Category category;
    private int cantidad;

    public CategoryStatistics(Category category, int cantidad) {
        this.category = category;
        this.cantidad = cantidad;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
