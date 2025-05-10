# Parser & Building an Abstract Syntax Tree
## Course: Formal Languages & Finite Automata
## Author: Nicologlo Victoria
## Group: FAF-233

## Structure

This project simulates how a compiler or interpreter would understand a mathematical expression or assignment. It involves the following components:

- **Lexer (Tokenizer)** – Breaks input strings into tokens.
- **Parser** – Converts tokens into an AST using grammar rules and precedence.
- **Expression Classes** – Java classes that model the tree nodes.
- **Main** – The entry point that ties everything together and prints the AST.


## Implementation

### 1. Lexer

The lexer scans the input character by character and creates a list of tokens. Each token represents a piece of the input: numbers, operators, identifiers, parentheses, or assignment.

Example:

Input: x = 1 + sin(2)
Tokens: IDENTIFIER(x), ASSIGN(=), NUMBER(1), OPERATOR(+), IDENTIFIER(sin), LPAREN, NUMBER(2), RPAREN



Key methods:

```java
public class Lexer {
    public List<Token> tokenize() {
        // Reads input and converts it to a list of tokens
    }
}
```
### 2. Parser
The parser builds an AST from the list of tokens using recursive descent parsing and operator precedence.

Structure:
```java
public class Parser {
    public Expression parseAssignment() {
        // Parses assignments like: x = expression
    }

    private Expression expression() {
        // Begins parsing at the highest level (additive)
    }

    private Expression parseAdditive() { /* handles + and - */ }
    private Expression parseMultiplicative() { /* handles * and / */ }
    private Expression parseUnary() { /* handles unary + and - */ }
    private Expression parsePrimary() {
        // Handles numbers, variables, parentheses, and function calls
    }
}
```

It supports nesting and proper order of operations (e.g. 1 + 2 * 3 is parsed correctly with multiplication having higher precedence than addition).

### 3. Expression Classes (AST Nodes)
All node types implement the Expression interface and override the print(String indent) method.

Types of nodes:

- NumberExpression

- VariableExpression

- UnaryExpression

- BinaryExpression

- FunctionExpression

- AssignmentExpression

### Example output for the input x = 1 + sin(2):

Assignment:

  Variable: x
  
  Expression:
  
    Binary Operation: +
    
      Left:
      
        Number: 1.0
        
      Right:
      
        Function: sin
        
          Number: 2.0
          
This shows the tree's hierarchy and structure.

### Features
Tokenizes numbers, variables, functions

Handles binary operators: +, -, *, /

Supports unary operators: +, -

Parses functions like sin, cos, etc.

Recognizes parentheses and assignment

Outputs a readable tree representation

## Conclusion
This project is a hands-on way to understand how expressions are parsed and structured into a tree. It mirrors how compilers interpret code under the hood — by tokenizing, parsing, and building syntax trees.

The code is modular and easy to extend, making it a solid base for further development — like evaluating expressions, adding variable environments, or compiling into bytecode.
