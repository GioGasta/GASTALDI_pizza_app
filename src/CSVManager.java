import java.io.*;
import java.util.*;

public class CSVManager
{
    private static final String FILE_PATH = "pizze.csv";

    public static void salva(List<Pizza> pizze) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Pizza p : pizze) {
                writer.write(p.nome + "," + p.ingredienti + "," + p.prezzo);
                writer.newLine();
            }
        }
    }

    public static List<Pizza> carica() throws IOException {
        List<Pizza> pizze = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return pizze;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    float prezzo = Float.parseFloat(parts[2]);
                    pizze.add(new Pizza(parts[0], parts[1], prezzo));
                }
            }
        }
        return pizze;
    }
}
