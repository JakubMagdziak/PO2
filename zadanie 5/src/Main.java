import java.util.*;

class NrTelefoniczny implements Comparable<NrTelefoniczny> {
    private String nrKierunkowy;
    private String nrTelefonu;

    public NrTelefoniczny(String nrKierunkowy, String nrTelefonu) {
        this.nrKierunkowy = nrKierunkowy;
        this.nrTelefonu = nrTelefonu;
    }
//test
    @Override
    public int compareTo(NrTelefoniczny o) {
        int result = this.nrKierunkowy.compareTo(o.nrKierunkowy);
        if (result == 0) {
            return this.nrTelefonu.compareTo(o.nrTelefonu);
        }
        return result;
    }

    public String getNrKierunkowy() {
        return nrKierunkowy;
    }

    public String getNrTelefonu() {
        return nrTelefonu;
    }
}

abstract class Wpis {
    abstract void opis();
}

class Osoba extends Wpis {
    private String imie;
    private String nazwisko;
    private String adres;

    private NrTelefoniczny nrTelefonu;

    public Osoba(String imie, String nazwisko, String adres, NrTelefoniczny nrTelefonu) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.adres = adres;
        this.nrTelefonu = nrTelefonu;
    }

    public String getAdres() {
        return adres;
    }

    @Override
    void opis() {
        System.out.println("Osoba: " + imie + " " + nazwisko + ", adres: " + adres + ", nr telefonu: " + nrTelefonu.getNrKierunkowy() + nrTelefonu.getNrTelefonu());
    }
}


class Firma extends Wpis {
    private String nazwa;
    private String adres;
    private NrTelefoniczny nrTelefonu;

    public Firma(String nazwa, String adres, NrTelefoniczny nrTelefonu) {
        this.nazwa = nazwa;
        this.adres = adres;
        this.nrTelefonu = nrTelefonu;
    }

    @Override
    void opis() {
        System.out.println("Firma: " + nazwa + ", adres: " + adres + ", nr telefonu: " + nrTelefonu.getNrKierunkowy() + nrTelefonu.getNrTelefonu());
    }
}

public class Main {
    public static void main(String[] args) {
        TreeMap<NrTelefoniczny, Wpis> ksiazkaTelefoniczna = new TreeMap<>();

        ksiazkaTelefoniczna.put(new NrTelefoniczny("48", "123456789"), new Osoba("Jan", "Kowalski", "ul. Prosta 1", new NrTelefoniczny("48", "123456789")));
        ksiazkaTelefoniczna.put(new NrTelefoniczny("48", "987654321"), new Osoba("Anna", "Nowak", "ul. Kwiatowa 2", new NrTelefoniczny("48", "987654321")));
        ksiazkaTelefoniczna.put(new NrTelefoniczny("48", "555666777"), new Firma("ABC Sp. z o.o.", "ul. Prosta 1", new NrTelefoniczny("48", "555666777")));
        ksiazkaTelefoniczna.put(new NrTelefoniczny("48", "333444555"), new Firma("XYZ Sp. z o.o.", "ul. Jasna 3", new NrTelefoniczny("48", "333444555")));

        System.out.println("Książka telefoniczna:");
        for (Map.Entry<NrTelefoniczny, Wpis> entry : ksiazkaTelefoniczna.entrySet()) {
            entry.getValue().opis();
        }

        Iterator<Map.Entry<NrTelefoniczny, Wpis>> iterator = ksiazkaTelefoniczna.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<NrTelefoniczny, Wpis> entry = iterator.next();
            if (entry.getValue() instanceof Osoba) {
                Osoba osoba = (Osoba) entry.getValue();
                String[] parts = osoba.getAdres().split(" ");
                String ulica = parts[0];
                for (Map.Entry<NrTelefoniczny, Wpis> otherEntry : ksiazkaTelefoniczna.entrySet()) {
                    if (otherEntry.getValue() instanceof Osoba) {
                        Osoba otherOsoba = (Osoba) otherEntry.getValue();
                        String[] otherParts = otherOsoba.getAdres().split(" ");
                        String otherUlica = otherParts[0];
                        if (ulica.equals(otherUlica) && !entry.getKey().equals(otherEntry.getKey())) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("\nKsiążka telefoniczna po eliminacji wpisów z identyczną nazwą ulicy:");
        for (Map.Entry<NrTelefoniczny, Wpis> entry : ksiazkaTelefoniczna.entrySet()) {
            entry.getValue().opis();
        }
    }
}
