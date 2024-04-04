import java.io.*;
import java.util.*;

class Product {
    String category;
    String name;

    public Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public String toString() {
        return category + ": " + name;
    }
}
public class ShoppingListManager {
    private static final String FILENAME = "shopping_list.txt";
    private static List<Product> shoppingList = new ArrayList<>();
    private static List<Product> userShoppingList = new ArrayList<>(); 

    public static void main(String[] args) {
        System.out.println("Aktualny katalog roboczy: " + System.getProperty("user.dir"));
        loadShoppingList();

        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.println("\nMenu:");
            System.out.println("1. Dodaj produkt do listy zakupów");
            System.out.println("2. Wyświetl wszystkie produkty z listy zakupów");
            System.out.println("3. Wyświetl produkty z danej kategorii");
            System.out.println("4. Usuń wszystkie produkty z listy zakupów");
            System.out.println("5. Usuń produkty z danej kategorii");
            System.out.println("6. Usuń produkt z listy zakupów");
            System.out.println("7. Zapisz listę zakupów na dysku");
            System.out.println("8. Wyjdź");

            System.out.print("Wybierz opcję: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    displayAllProducts();
                    break;
                case 3:
                    displayProductsByCategory(scanner);
                    break;
                case 4:
                    clearShoppingList();
                    break;
                case 5:
                    removeProductsByCategory(scanner);
                    break;
                case 6:
                    removeProduct(scanner);
                    break;
                case 7:
                    saveShoppingList();
                    break;
                case 8:
                    exit = true;
                    break;
                default:
                    System.out.println("Nieprawidłowa opcja.");
            }
        }
    }

    private static void loadShoppingList() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String category = "";
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); 
                if (!line.isEmpty()) { 
                    if (line.startsWith(".")) {
                        category = line.substring(1); 
                    } else if (line.contains("-")) {
                        String[] parts = line.split("-");
                        if (parts.length >= 2) {
                            String name = parts[1].trim();
                            shoppingList.add(new Product(category, name));
                        } else {
                            System.out.println("Błędny format danych w linii: " + line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Błąd podczas wczytywania listy zakupów głównej.");
            e.printStackTrace();
        }

       
        final String USER_SHOPPING_LIST_FILENAME = "lista_na_zakupy.txt";
        File userShoppingListFile = new File(USER_SHOPPING_LIST_FILENAME);
        if (userShoppingListFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(USER_SHOPPING_LIST_FILENAME))) {
                String category = "";
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        if (line.startsWith(".")) {
                            category = line.substring(1);
                        } else if (line.contains("-")) {
                            String[] parts = line.split("-");
                            if (parts.length >= 2) {
                                String name = parts[1].trim();
                                userShoppingList.add(new Product(category, name));
                            } else {
                                System.out.println("Błędny format danych w linii: " + line);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Błąd podczas wczytywania listy zakupów użytkownika.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Nie znaleziono listy zakupów użytkownika. Tworzenie nowej listy.");
        }
    }


    private static Set<String> getCategories(List<Product> productList) {
        Set<String> categories = new HashSet<>();
        for (Product product : productList) {
            categories.add(product.category);
        }
        return categories;
    }


    private static void addProduct(Scanner scanner) {
        Set<String> categories = getCategories(shoppingList);

        System.out.println("Dostępne kategorie:");
        for (String category : categories) {
            System.out.println("- " + category);
        }

        System.out.print("Wybierz kategorię: ");
        String selectedCategory = scanner.nextLine().trim();

        if (!categories.contains(selectedCategory)) {
            System.out.println("Wybrana kategoria jest nieprawidłowa.");
            return;
        }

        System.out.println("Dostępne produkty w kategorii " + selectedCategory + ":");
        for (Product product : shoppingList) {
            if (product.category.equals(selectedCategory)) {
                System.out.println("- " + product.name);
            }
        }

        System.out.print("Wybierz produkt: ");
        String selectedProduct = scanner.nextLine().trim().toLowerCase();


        for (Product product : userShoppingList) {
            if (product.category.equals(selectedCategory) && product.name.toLowerCase().equals(selectedProduct)) {
                System.out.println("Produkt już znajduje się na liście zakupów.");
                return;
            }
        }


        for (Product product : shoppingList) {
            if (product.category.equals(selectedCategory) && product.name.toLowerCase().equals(selectedProduct)) {
                userShoppingList.add(new Product(selectedCategory, product.name)); // Dodaj produkt do listy zakupów użytkownika
                System.out.println("Produkt dodany do listy zakupów.");
                return;
            }
        }

        System.out.println("Wybrany produkt jest nieprawidłowy.");
    }



    private static void displayAllProducts() {
        if (userShoppingList.isEmpty()) {
            System.out.println("Lista zakupów jest pusta.");
        } else {
            Map<String, List<String>> productsByCategory = new HashMap<>();
            for (Product product : userShoppingList) {
                if (!productsByCategory.containsKey(product.category)) {
                    productsByCategory.put(product.category, new ArrayList<>());
                }
                productsByCategory.get(product.category).add(product.name);
            }

            for (Map.Entry<String, List<String>> entry : productsByCategory.entrySet()) {
                System.out.println(entry.getKey() + ":");
                for (String productName : entry.getValue()) {
                    System.out.println("- " + productName);
                }
            }
        }
    }



    private static void displayProductsByCategory(Scanner scanner) {
        System.out.println("Dostępne kategorie:");
        Set<String> categories = getCategories(userShoppingList);
        for (String category : categories) {
            System.out.println(category);
        }

        System.out.print("Wybierz kategorię: ");
        String selectedCategory = scanner.nextLine().trim();

        System.out.println("Produkty z kategorii " + selectedCategory + ":");
        for (Product product : userShoppingList) { 
            if (product.category.equals(selectedCategory)) {
                System.out.println("- " + product.name);
            }
        }
    }


    private static void clearShoppingList() {
        userShoppingList.clear();
        System.out.println("Lista zakupów została wyczyszczona.");
    }
    private static void removeProductsByCategory(Scanner scanner) {
        System.out.println("Dostępne kategorie:");
        Set<String> categories = getCategories(userShoppingList);
        for (String category : categories) {
            System.out.println(category);
        }

        System.out.print("Wybierz kategorię: ");
        String selectedCategory = scanner.nextLine().trim();

        userShoppingList.removeIf(product -> product.category.equals(selectedCategory));
        System.out.println("Produkty z kategorii " + selectedCategory + " zostały usunięte.");
    }

    private static void removeProduct(Scanner scanner) {
        System.out.println("Dostępne kategorie:");
        Set<String> categories = getCategories(userShoppingList);
        for (String category : categories) {
            System.out.println(category);
        }

        System.out.print("Wybierz kategorię: ");
        String selectedCategory = scanner.nextLine().trim();

        System.out.println("Dostępne produkty w kategorii " + selectedCategory + ":");
        for (Product product : userShoppingList) {
            if (product.category.equals(selectedCategory)) {
                System.out.println(product.name);
            }
        }

        System.out.print("Wybierz produkt do usunięcia: ");
        String selectedProduct = scanner.nextLine().trim();

        userShoppingList.removeIf(product -> product.category.equals(selectedCategory) && product.name.equals(selectedProduct));
        System.out.println("Produkt został usunięty z listy zakupów.");
    }

    private static void saveShoppingList() {
        final String USER_SHOPPING_LIST_FILENAME = "lista_na_zakupy.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_SHOPPING_LIST_FILENAME))) {
            Set<String> categories = getCategories(userShoppingList);

            for (String category : categories) {
                writer.println("." + category); 
                for (Product product : userShoppingList) {
                    if (product.category.equals(category)) {
                        writer.println("-" + product.name);
                    }
                }
            }
            System.out.println("Lista zakupów użytkownika została zapisana w pliku: " + USER_SHOPPING_LIST_FILENAME);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisywania listy zakupów użytkownika.");
            e.printStackTrace();
        }
    }

}


