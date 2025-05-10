import java.util.List;
import java.util.Scanner;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter expression: ");
        String input = sc.nextLine();
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        Parser parser = new Parser(tokens);
        Expression ast = parser.parseAssignment();
        System.out.println("\nStatement:");
        System.out.println(ast.format());
        System.out.println("\nAST Tree:");
        ast.print("");
    }
}

