public class VariableExpression implements Expression {
    String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Variable: " + name);
    }

    @Override
    public String format() {
        return name;
    }
}