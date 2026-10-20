

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int PORT = 12345; // Puerto de conexión

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("====================================");
            System.out.println("     Servicio Backend Iniciado      ");
            System.out.println("Escuchando en el puerto: " + PORT);
            System.out.println("====================================");

            // Bucle infinito para mantener el servicio activo escuchando clientes (Frontend)
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());

                // Aquí creas y arrancas el hilo (Thread) o ServerWorker que atiende al cliente
                // new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor Backend: " + e.getMessage());
        }
    }
}