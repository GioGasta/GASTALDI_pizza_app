import com.google.gson.Gson;
import okhttp3.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private final OkHttpClient client;
    private final Gson gson;
    private final Scanner scanner;
    private final String BASE_URL = "https://crudcrud.com/api/13c7177d18d3415e8651f13a383b8131/pizze";

    public App() {
        client = new OkHttpClient();
        gson = new Gson();
        scanner = new Scanner(System.in);
    }

    // GET – Visualizza menù da crudcrud o CSV
    public void doGet() throws IOException {
        Request request = new Request.Builder().url(BASE_URL).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Pizza[] pizze = gson.fromJson(response.body().string(), Pizza[].class);

            if (pizze.length == 0) {
                System.out.println("Nessuna pizza trovata su crudcrud.");
            } else {
                for (Pizza pizza : pizze) {
                    System.out.println(pizza);
                }
            }

            // salva backup locale
            CSVManager.salva(Arrays.asList(pizze));

        } catch (IOException e) {
            System.out.println("⚠️ Errore con crudcrud, provo a caricare backup locale...");
            try {
                List<Pizza> pizze = CSVManager.carica();
                if (pizze.isEmpty()) System.out.println("Nessun dato locale trovato.");
                else pizze.forEach(System.out::println);
            } catch (IOException ex) {
                System.out.println("Errore nel caricamento CSV: " + ex.getMessage());
            }
        }
    }

    // POST – Aggiunge una nuova pizza
    public void doPost(Pizza pizza) throws IOException {
        String json = gson.toJson(pizza);

        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Errore nella POST: " + response);
        }
    }

    // DELETE – Elimina una pizza per id
    public void doDelete(String id) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Errore nella DELETE: " + response);
        }
    }

    // Loop principale con menù
    public void run() {
        while (true) {
            System.out.println("\n--- MENU PIZZERIA ---");
            System.out.println("1 - Visualizza menu");
            System.out.println("2 - Aggiungi pizza");
            System.out.println("3 - Elimina pizza");
            System.out.println("0 - Esci");
            System.out.print("Scelta: ");

            String scelta = scanner.nextLine();

            try {
                switch (scelta) {
                    case "1" -> doGet();
                    case "2" -> {
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Ingredienti: ");
                        String ingr = scanner.nextLine();
                        System.out.print("Prezzo: ");
                        float prezzo = Float.parseFloat(scanner.nextLine());

                        Pizza nuova = new Pizza(nome, ingr, prezzo);
                        doPost(nuova);
                        System.out.println("✅ Pizza aggiunta con successo!");
                    }
                    case "3" -> {
                        // prima mostriamo le pizze per scegliere quale eliminare
                        Request req = new Request.Builder().url(BASE_URL).build();
                        Response res = client.newCall(req).execute();
                        Pizza[] pizze = gson.fromJson(res.body().string(), Pizza[].class);

                        if (pizze.length == 0) {
                            System.out.println("Nessuna pizza da eliminare.");
                            break;
                        }

                        for (int i = 0; i < pizze.length; i++) {
                            System.out.println((i + 1) + " - " + pizze[i]);
                        }

                        System.out.print("Numero pizza da eliminare: ");
                        int idx = Integer.parseInt(scanner.nextLine()) - 1;

                        if (idx < 0 || idx >= pizze.length) {
                            System.out.println("Indice non valido.");
                            break;
                        }

                        doDelete(pizze[idx]._id); // campo id di crudcrud
                        System.out.println("🗑️ Pizza eliminata!");
                    }
                    case "0" -> {
                        System.out.println("Arrivederci!");
                        return;
                    }
                    default -> System.out.println("Scelta non valida!");
                }
            } catch (IOException e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }
}