
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Converter {

    // Java keywords that cannot be used as variable names
    private static final Set<String> JAVA_KEYWORDS = new HashSet<>(
        Arrays.asList(
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const", "continue",
            "default", "do", "double", "else", "enum", "extends",
            "final", "finally", "float", "for", "goto", "if",
            "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "package", "private", "protected",
            "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw",
            "throws", "transient", "try", "void", "volatile", "while",
            "true", "false", "null", "_", "var", "yield", "record",
            "sealed", "permits", "non-sealed"
        )
    );

    public String convertLine(String delphiCode) {

        // 1. Check for empty input
        if (delphiCode == null || delphiCode.trim().isEmpty()) {
            return "Please enter a Delphi statement.";
        }

        delphiCode = delphiCode.trim();
        String lowerCode = delphiCode.toLowerCase();

        // 2. Convert Writeln and ShowMessage
        if (lowerCode.startsWith("writeln")
                || lowerCode.startsWith("showmessage")) {

            int openPosition = delphiCode.indexOf('(');
            int closePosition = delphiCode.lastIndexOf(')');

            if (openPosition == -1
                    || closePosition <= openPosition
                    || !delphiCode.endsWith(";")) {
                return "Invalid Delphi statement.";
            }

            String message = delphiCode.substring(
                    openPosition + 1, closePosition).trim();

            message = message.replace('\'', '"');

            return "System.out.println(" + message + ");";
        }

        // 3. Find variable assignment
        int equalPosition = delphiCode.indexOf(":=");

        if (equalPosition == -1) {
            return "Unsupported Delphi statement.";
        }

        String variableName =
                delphiCode.substring(0, equalPosition).trim();

        String value =
                delphiCode.substring(equalPosition + 2).trim();

        // Remove ending semicolon
        if (value.endsWith(";")) {
            value = value.substring(0, value.length() - 1).trim();
        }

        // 4. Validate variable name
        if (!variableName.matches("[a-zA-Z_$][a-zA-Z0-9_$]*")) {
            return "Invalid variable name.";
        }

        // 5. Reject Java keywords
        if (JAVA_KEYWORDS.contains(variableName)) {
            return "Invalid variable name: Java keyword.";
        }
        
        // 6. Check empty value
        if (value.isEmpty()) {
            return "Invalid variable assignment.";
        }

        // 7. Convert string assignment
        if (value.length() >= 2
                && value.startsWith("'")
                && value.endsWith("'")) {

            value = value.substring(1, value.length() - 1);
            value = value.replace("\"", "\\\"");

            return "String " + variableName + " = \"" + value + "\";";
        }

        // 8. Convert integer assignment
        if (value.matches("-?\\d+")) {
            return "int " + variableName + " = " + value + ";";
        }

        // 9. Convert decimal assignment
        if (value.matches("-?\\d+\\.\\d+")) {
            return "double " + variableName + " = " + value + ";";
        }

        return "Invalid value. Enter a valid string, integer, or decimal.";
    }
}
