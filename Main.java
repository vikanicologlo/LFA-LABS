import java.util.ArrayList;
import java.util.List;

enum TokenType {
    ASSIGN, INTEGER, FLOAT, IDENTIFIER, EOF, EOL,
    PLUS, MINUS, MULTIPLY, DIVIDE, SIN, COS,
    LPAREN, RPAREN;
}

class Token {
    TokenType type;
    String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token(" + type + ", " + value + ")";
    }
}

class Lexer {
    private final String text;
    private int pos;
    private Character currentChar;

    public Lexer(String text) {
        this.text = text;
        this.pos = 0;
        this.currentChar = text.isEmpty() ? null : text.charAt(0);
    }

    private void moveForward() {
        pos++;
        currentChar = (pos < text.length()) ? text.charAt(pos) : null;
    }

    private void skipWhitespace() {
        while (currentChar != null && Character.isWhitespace(currentChar) && currentChar != '\n') {
            moveForward();
        }
    }

    private Token number() {
        StringBuilder result = new StringBuilder();
        boolean isFloat = false;

        if (currentChar == '-') {
            result.append(currentChar);
            moveForward();
        }

        while (currentChar != null && Character.isDigit(currentChar)) {
            result.append(currentChar);
            moveForward();
        }

        if (currentChar != null && currentChar == '.') {
            isFloat = true;
            result.append(currentChar);
            moveForward();
            while (currentChar != null && Character.isDigit(currentChar)) {
                result.append(currentChar);
                moveForward();
            }
        }

        return new Token(isFloat ? TokenType.FLOAT : TokenType.INTEGER, result.toString());
    }

    private Token identifier() {
        StringBuilder result = new StringBuilder();
        while (currentChar != null && (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
            result.append(currentChar);
            moveForward();
        }
        String id = result.toString();
        if (id.equals("sin")) return new Token(TokenType.SIN, id);
        if (id.equals("cos")) return new Token(TokenType.COS, id);
        return new Token(TokenType.IDENTIFIER, id);
    }

    public Token getNextToken() {
        while (currentChar != null) {
            if (Character.isWhitespace(currentChar)) {
                if (currentChar == '\n') {
                    moveForward();
                    return new Token(TokenType.EOL, "\n");
                }
                skipWhitespace();
                continue;
            }

            if (Character.isDigit(currentChar) || currentChar == '-') return number();
            if (Character.isLetter(currentChar)) return identifier();

            switch (currentChar) {
                case '+': moveForward(); return new Token(TokenType.PLUS, "+");
                case '-': moveForward(); return new Token(TokenType.MINUS, "-");
                case '*': moveForward(); return new Token(TokenType.MULTIPLY, "*");
                case '/': moveForward(); return new Token(TokenType.DIVIDE, "/");
                case '=': moveForward(); return new Token(TokenType.ASSIGN, "=");
                case '(': moveForward(); return new Token(TokenType.LPAREN, "(");
                case ')': moveForward(); return new Token(TokenType.RPAREN, ")");
                default: throw new RuntimeException("Invalid character: " + currentChar);
            }
        }
        return new Token(TokenType.EOF, null);
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.print("Введите выражение: ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String userInput = scanner.nextLine();
        scanner.close();

        Lexer lexer = new Lexer(userInput);
        List<Token> tokens = new ArrayList<>();

        Token token;
        do {
            token = lexer.getNextToken();
            tokens.add(token);
        } while (token.type != TokenType.EOF);

        for (Token t : tokens) {
            System.out.print(t + " ");
        }
    }
}
