public class UnaryExpression implements Expression {
    char operator;
    Expression expr;

    public UnaryExpression(char operator, Expression expr) {
        this.operator = operator;
        this.expr = expr;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Unary Operation: " + operator);
        expr.print(indent + "  ");
    }
    @Override
    public String format() {
        return "[" + operator + expr.format() + "]";
    }
}