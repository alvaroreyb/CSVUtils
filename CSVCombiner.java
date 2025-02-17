import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CSVCombiner {

    static class Persona {
        String apellido;
        String nombre;
        String email;

        public Persona(String apellido, String nombre, String email) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
        }

        @Override
        public int hashCode() {
            return email == null ? 0 : email.toLowerCase().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Persona other = (Persona) obj;
            return email != null && other.email != null && email.equalsIgnoreCase(other.email);
        }

        @Override
        public String toString() {
            return apellido + "," + nombre + "," + email;
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java CSVCombiner archivo1.csv archivo2.csv [archivoSalida.csv]");
            System.exit(1);
        }

        String file1 = args[0];
        String file2 = args[1];
        String outputFile = (args.length >= 3) ? args[2] : "resultado.csv";
        Set<Persona> personas = new HashSet<>();
        procesarArchivo(file1, personas);
        procesarArchivo(file2, personas);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            bw.write("apellido,nombre,email");
            bw.newLine();
            for (Persona p : personas) {
                bw.write(p.toString());
                bw.newLine();
            }
            System.out.println("Archivo combinado generado: " + outputFile);
        } catch (IOException e) {
            System.err.println("Error al escribir en " + outputFile + ": " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Método que procesa un archivo CSV, leyendo cada línea (saltando el encabezado)
     * y añadiendo los registros al conjunto de personas.
     */
    private static void procesarArchivo(String fileName, Set<Persona> personas) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean primerLinea = true;
            while ((line = br.readLine()) != null) {
                if (primerLinea) {
                    primerLinea = false;
                    continue;
                }
                String[] partes = line.split(",");
                if (partes.length < 3) {
                    continue;
                }
                String nombre = partes[0].trim();
                String apellido = partes[1].trim();
                String email = partes[2].trim();
                if (email.isEmpty()) {
                    continue;
                }
                Persona persona = new Persona(nombre, apellido, email);
                personas.add(persona);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo " + fileName + ": " + e.getMessage());
            System.exit(1);
        }
    }
}
