import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Podaj treść notyfikacji: ");
            String notification = userInput.readLine();
            System.out.print("Podaj czas odesłania notyfikacji (HH:MM): ");
            String time = userInput.readLine();

            // Walidacja czasu odesłania notyfikacji
            if (!time.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                throw new IllegalArgumentException("Nieprawidłowy format czasu. Wprowadź w formacie HH:MM.");
            }

            output.println(notification);
            output.println(time);

            String receivedNotification = input.readLine();
            System.out.println("Otrzymana notyfikacja: " + receivedNotification);

            socket.close();
        } catch (IOException e) {
            System.err.println("Błąd podczas komunikacji z serwerem: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Błąd walidacji danych: " + e.getMessage());
        }
    }
}
