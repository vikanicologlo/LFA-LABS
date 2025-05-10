import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token get(int offset) {
        if (pos + offset >= tokens.size()) return new Token(TokenType.EOF, "");
        return tokens.get(pos + offset);
    }

    private Token consume(TokenType type) {
        Token current = get(0);
        if (current.type != type)
            throw new RuntimeException("Expected " + type + " but found " + current.type);
        pos++;
        return current;
    }

    public Expression parseAssignment() {
        Token next = get(0);
        if (next.type == TokenType.IDENTIFIER && get(1).type == TokenType.ASSIGN) {
            String varName = next.value;
            consume(TokenType.IDENTIFIER);
            consume(TokenType.ASSIGN);
            Expression expr = expression();
            return new AssignmentExpression(varName, expr);
        } else {
            return expression();
        }
    }

    private Expression expression() {
        return parseAdditive();
    }

    private Expression parseAdditive() {
        Expression result = parseMultiplicative();
        while (get(0).type == TokenType.OPERATOR && (get(0).value.equals("+") || get(0).value.equals("-"))) {
            char op = get(0).value.charAt(0);
            consume(TokenType.OPERATOR);
            Expression right = parseMultiplicative();
            result = new BinaryExpression(op, result, right);
        }
        return result;
    }

    private Expression parseMultiplicative() {
        Expression result = parseUnary();
        while (get(0).type == TokenType.OPERATOR && (get(0).value.equals("*") || get(0).value.equals("/"))) {
            char op = get(0).value.charAt(0);
            consume(TokenType.OPERATOR);
            Expression right = parseUnary();
            result = new BinaryExpression(op, result, right);
        }
        return result;
    }

    private Expression parseUnary() {
        if (get(0).type == TokenType.OPERATOR && (get(0).value.equals("+") || get(0).value.equals("-"))) {
            char op = get(0).value.charAt(0);
            consume(TokenType.OPERATOR);
            Expression expr = parsePrimary();
            return new UnaryExpression(op, expr);
        }
        return parsePrimary();
    }

    private Expression parsePrimary() {
        Token current = get(0);
        if (current.type == TokenType.NUMBER) {
            consume(TokenType.NUMBER);
            return new NumberExpression(Double.parseDouble(current.value));
        } else if (current.type == TokenType.IDENTIFIER) {
            String name = current.value;
            if (get(1).type == TokenType.LPAREN) {
                consume(TokenType.IDENTIFIER);
                consume(TokenType.LPAREN);
                Expression arg = expression();
                consume(TokenType.RPAREN);
                return new FunctionExpression(name, arg);
            } else {
                consume(TokenType.IDENTIFIER);
                return new VariableExpression(name);
            }
        } else if (current.type == TokenType.LPAREN) {
            consume(TokenType.LPAREN);
            Expression expr = expression();
            consume(TokenType.RPAREN);
            return expr;
        } else {
            throw new RuntimeException("Unexpected token: " + current.value);
        }
    }
}
