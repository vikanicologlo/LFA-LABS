public class NumberExpression implements Expression {
    double value;

    public NumberExpression(double value) {
        this.value = value;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Number: " + value);
    }

    @Override
    public String format() {
        return String.valueOf(value);
    }
}