import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UniqueEmailExtractor {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java EmailCleaner archivo1.txt archivo2.txt [archivoSalida.txt]");
            System.exit(1);
        }

        String file1 = args[0];
        String file2 = args[1];
        String outputFile = (args.length >= 3) ? args[2] : "resultado.txt";
        Set<String> removalSet = new HashSet<>();

        Pattern emailPattern = Pattern.compile("([a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7})");
        try (BufferedReader br = new BufferedReader(new FileReader(file2))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = emailPattern.matcher(line);
                while (matcher.find()) {
                    String email = matcher.group().toLowerCase();
                    removalSet.add(email);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo " + file2 + ": " + e.getMessage());
            System.exit(1);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file1));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringBuffer sb = new StringBuffer();
                Matcher matcher = emailPattern.matcher(line);
                while (matcher.find()) {
                    String emailFound = matcher.group().toLowerCase();
                    if (removalSet.contains(emailFound)) {
                        matcher.appendReplacement(sb, "");
                    } else {
                        matcher.appendReplacement(sb, matcher.group());
                    }
                }
                matcher.appendTail(sb);
                bw.write(sb.toString());
                bw.newLine();
            }
            System.out.println("Archivo de salida generado: " + outputFile);
        } catch (IOException e) {
            System.err.println("Error al procesar el archivo " + file1 + ": " + e.getMessage());
            System.exit(1);
        }
    }
}
