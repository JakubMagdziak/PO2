import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

class WektoryRoznejDlugosciException extends Exception {
    private int dlugosc1;
    private int dlugosc2;

    public WektoryRoznejDlugosciException(int dlugosc1, int dlugosc2) {
        this.dlugosc1 = dlugosc1;
        this.dlugosc2 = dlugosc2;
    }

    public int getDlugosc1() {
        return dlugosc1;
    }

    public int getDlugosc2() {
        return dlugosc2;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] wektor1 = wczytajWektor(scanner);
        int[] wektor2 = wczytajWektor(scanner);

        try {
            dodajWektory(wektor1, wektor2);
            zapiszWynik(wektor1, wektor2);
        } catch (WektoryRoznejDlugosciException e) {
            System.out.println("Wektory mają różne długości. Długość pierwszego wektora to " + e.getDlugosc1() +
                    ", a drugiego to " + e.getDlugosc2() + ". Proszę wprowadzić wektory ponownie.");
            wektor1 = wczytajWektor(scanner);
            wektor2 = wczytajWektor(scanner);
            try {
                dodajWektory(wektor1, wektor2);
                zapiszWynik(wektor1, wektor2);
            } catch (WektoryRoznejDlugosciException ex) {
                System.out.println("Wektory nadal mają różne długości. Koniec programu.");
            }
        }
    }

    private static int[] wczytajWektor(Scanner scanner) {
        System.out.println("Podaj wektor (pojedyncze liczby oddzielaj spacją, zakończ enterem):");
        String input = scanner.nextLine();
        String[] tokens = input.split("\\s+");
        int[] wektor = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            try {
                wektor[i] = Integer.parseInt(tokens[i]);
            } catch (NumberFormatException e) {
                // Ignorowanie niepoprawnych wartości
            }
        }
        return wektor;
    }

    private static void dodajWektory(int[] wektor1, int[] wektor2) throws WektoryRoznejDlugosciException {
        if (wektor1.length != wektor2.length) {
            throw new WektoryRoznejDlugosciException(wektor1.length, wektor2.length);
        }
        for (int i = 0; i < wektor1.length; i++) {
            wektor1[i] += wektor2[i];
        }
    }

    private static void zapiszWynik(int[] wektor1, int[] wektor2) {
        try (FileWriter writer = new FileWriter("wynik.txt")) {
            writer.write("Wynik dodawania wektorów:\n");
            for (int i = 0; i < wektor1.length; i++) {
                writer.write((wektor1[i] + wektor2[i]) + " ");
            }
            writer.write("\n");
            System.out.println("Wynik dodawania zapisany do pliku 'wynik.txt'.");
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisywania wyniku.");
            e.printStackTrace();
        }
    }
}
