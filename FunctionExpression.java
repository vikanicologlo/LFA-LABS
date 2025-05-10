public class FunctionExpression implements Expression {
    String name;
    Expression argument;

    public FunctionExpression(String name, Expression argument) {
        this.name = name;
        this.argument = argument;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Function: " + name);
        argument.print(indent + "  ");
    }
    @Override
    public String format() {
        return name + "(" + argument.format() + ")";
    }

}