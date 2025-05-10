import java.util.*;

public class Lexer {
    private final String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char ch = input.charAt(pos);
            if (Character.isWhitespace(ch)) {
                pos++;
            } else if (Character.isDigit(ch)) {
                StringBuilder number = new StringBuilder();
                while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
                    number.append(input.charAt(pos++));
                }
                tokens.add(new Token(TokenType.NUMBER, number.toString()));
            } else if (Character.isLetter(ch)) {
                StringBuilder ident = new StringBuilder();
                while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) {
                    ident.append(input.charAt(pos++));
                }
                tokens.add(new Token(TokenType.IDENTIFIER, ident.toString()));
            } else if ("+-*/".indexOf(ch) != -1) {
                tokens.add(new Token(TokenType.OPERATOR, Character.toString(ch)));
                pos++;
            } else if (ch == '(') {
                tokens.add(new Token(TokenType.LPAREN, "("));
                pos++;
            } else if (ch == ')') {
                tokens.add(new Token(TokenType.RPAREN, ")"));
                pos++;
            } else if (ch == '=') {
                tokens.add(new Token(TokenType.ASSIGN, "="));
                pos++;
            } else {
                throw new RuntimeException("Unknown character: " + ch);
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}
