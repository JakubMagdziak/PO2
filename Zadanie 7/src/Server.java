import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Serwer uruchomiony. Oczekiwanie na połączenia...");

            ExecutorService executor = Executors.newCachedThreadPool();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas uruchamiania serwera: " + e.getMessage());
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            String notification = input.readLine();
            String time = input.readLine();

            System.out.println("Otrzymano notyfikację: " + notification + ", czas: " + time);


            output.println("Wysłano notyfikację: " + notification);

            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Błąd podczas obsługi klienta: " + e.getMessage());
        }
    }
}
