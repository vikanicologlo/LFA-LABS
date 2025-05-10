public class BinaryExpression implements Expression {
    char operator;
    Expression left, right;

    public BinaryExpression(char operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Binary Operation: " + operator);
        System.out.println(indent + "  Left:");
        left.print(indent + "    ");
        System.out.println(indent + "  Right:");
        right.print(indent + "    ");
    }

    @Override
    public String format() {
        return "(" + left.format() + " " + operator + " " + right.format() + ")";
    }
}