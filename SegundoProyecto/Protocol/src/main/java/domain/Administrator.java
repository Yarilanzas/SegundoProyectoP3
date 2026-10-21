package domain;
import java.io.Serializable;

public class Administrator extends User implements Serializable{

    public Administrator() {
        super();
    }

    public Administrator(String id, String password) {
        super(id, password);
    }


}