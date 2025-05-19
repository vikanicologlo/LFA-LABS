# Parser & Building an Abstract Syntax Tree
## Course: Formal Languages & Finite Automata
## Author: Nicologlo Victoria
## Group: FAF-233

## Theory
In the context of formal languages and automata, parsing is the process of analyzing a string of symbols based on a given grammar. It plays a crucial role in compilers and interpreters by transforming source code into a structured representation called the Abstract Syntax Tree (AST).

The Lexer (Tokenizer) is the first stage of a compiler pipeline. It scans the raw input and converts it into a sequence of tokens, which are the smallest meaningful units (e.g., numbers, operators, identifiers, parentheses). Lexical analysis ensures that the input stream is properly segmented before further analysis.

The Parser takes these tokens and organizes them into a tree structure, the AST, using recursive descent parsing and operator precedence rules. Each node in the AST corresponds to a syntactic construct, such as binary operations, function calls, or assignments. Parsing also enforces grammatical rules, ensuring that expressions are syntactically valid.

The Abstract Syntax Tree (AST) abstracts away unnecessary syntactic details (e.g., parentheses), preserving only the hierarchical structure and semantic relationships. This tree is crucial for later stages such as evaluation, optimization, or code generation.

Theoretical foundations of this approach lie in context-free grammars (CFGs) and recursive algorithms. Parsing strategies like top-down parsing, LL parsing, or recursive descent are used to construct trees from grammar rules. The modular class design (e.g., BinaryExpression, FunctionExpression) reflects the recursive structure of grammar definitions.

This theory is applied in the project by implementing a lexer, parser, and expression classes in Java, providing insight into how languages are processed at a low level and how compilers interpret and represent code internally.


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
```
Assignment:

  Variable: x
  
  Expression:
  
    Binary Operation: +
    
      Left:
      
        Number: 1.0
        
      Right:
      
        Function: sin
        
          Number: 2.0
```

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
