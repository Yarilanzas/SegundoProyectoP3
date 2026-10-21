package domain;
import java.io.Serializable;

public class Response implements Serializable {
    private int status;          // Protocol.ERROR_NO_ERROR o Protocol.ERROR_ERROR
    private Object data;         //El objeto
    private String errorMessage; // Protocol.ERROR_ERROR = mensaje si ocurrió una excepción


    public Response(int status, Object data, String errorMessage) {
        this.status = status;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}