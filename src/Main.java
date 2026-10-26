
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Create the converter object
        Converter converter = new Converter();

        System.out.println("=== Delphi-to-Java Converter ===");
        System.out.print("Enter a Delphi statement: ");

        String delphiCode = scanner.nextLine();

        // Call the Converter class
        String javaCode = converter.convertLine(delphiCode);

        System.out.println("\nConverted Java code:");
        System.out.println(javaCode);

        scanner.close();
    }
}
