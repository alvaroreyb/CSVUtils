import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailIntersection {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java EmailIntersection archivo1.txt archivo2.txt");
            System.exit(1);
        }

        String file1 = args[0];
        String file2 = args[1];

        Set<String> emailsFile1 = new HashSet<>();
        Set<String> intersection = new HashSet<>();

        Pattern emailPattern = Pattern.compile("([a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7})");

        // Leemos el primer archivo y extraemos los correos.
        try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = emailPattern.matcher(line);
                while (matcher.find()) {
                    String email = matcher.group().toLowerCase();
                    emailsFile1.add(email);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + file1 + ": " + e.getMessage());
            System.exit(1);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file2))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = emailPattern.matcher(line);
                while (matcher.find()) {
                    String email = matcher.group().toLowerCase();
                    if (emailsFile1.contains(email)) {
                        intersection.add(email);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer " + file2 + ": " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Correos electrónicos que se repiten en ambos archivos:");
        for (String email : intersection) {
            System.out.println(email);
        }
    }
}
