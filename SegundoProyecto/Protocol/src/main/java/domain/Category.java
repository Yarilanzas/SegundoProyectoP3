package domain;
import java.io.Serializable;

public class Category implements Serializable {
    private String id;
    private String description;

    public Category() {
        this.id = "";
        this.description = "";

    }
    public Category(String id, String description) {
        this.id = id;
        this.description = description;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString(){
        return description;
    }
}