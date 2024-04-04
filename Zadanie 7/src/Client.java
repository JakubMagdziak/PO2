import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 12345);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Podaj treść notyfikacji (wpisz 'quit' aby wyjść): ");
                String notification = userInput.readLine();

                if ("quit".equalsIgnoreCase(notification)) {
                    break; // Wyjście z pętli w przypadku wpisania "quit"
                }

                String time;
                while(true){
                    System.out.print("Podaj czas odesłania notyfikacji do użytkownika (HH:MM): ");
                    time = userInput.readLine();
                    if (!time.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                        System.out.println("Nieprawidłowy format czasu. Wprowadź w formacie HH:MM.");
                        continue;
                    }
                    break;
                }


                output.println(notification);
                output.println(time);

                String receivedNotification = input.readLine();
                System.out.println("Otrzymana notyfikacja: " + receivedNotification);
            }

            socket.close();
        } catch (IOException e) {
            System.err.println("Błąd podczas komunikacji z serwerem: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Błąd walidacji danych: " + e.getMessage());
        }
    }
}
