
public class AssignmentExpression implements Expression {
    String variable;
    Expression expression;

    public AssignmentExpression(String variable, Expression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "Assignment:");
        System.out.println(indent + "  Variable: " + variable);
        System.out.println(indent + "  Expression:");
        expression.print(indent + "    ");
    }
    @Override
    public String format() {
        return variable + " = " + expression.format();
    }
}