# Laboratory Work #3: Lexer & Scanner
# Course: Formal Languages & Finite Automata
# Author: Nicologlo Victoria, FAF-233
# Professors: Cretu Dumitru, Irina Cojuhari

## Theory
Lexical analysis is the first stage in the compilation or interpretation of a language, where a lexer (also called a tokenizer or scanner) processes an input string and converts it into a sequence of tokens. A token consists of a type and an optional value, representing fundamental elements such as numbers, operators, identifiers, and keywords. The lexer scans the input character by character, grouping sequences based on predefined rules while ignoring irrelevant whitespace. In this lab, we implemented a lexer that can recognize arithmetic operators, assignment, integers, floating-point numbers, trigonometric functions (sin, cos), identifiers, parentheses for grouping, and special tokens like EOF and EOL. The lexer ensures correct token classification, enabling further processing by a parser or interpreter.

## Objectives
- Understand what **lexical analysis** is.
- Get familiar with the inner workings of a **lexer/scanner/tokenizer**.
- Implement a sample lexer and demonstrate how it works.

## Features
This project implements a lexer that recognizes:
- **Basic arithmetic operations**: Addition (+), subtraction (-), multiplication (*), and division (/).
- **Assignment operation**: `=`.
- **Numbers**: Both **integers** (e.g., `42`, `-7`) and **floating-point numbers** (e.g., `3.14`, `-0.5`).
- **Identifiers**: Variable names such as `x`, `result`, `value_1`.
- **Trigonometric functions**: `sin(x)`, `cos(x)`.
- **Parentheses**: `(` and `)`.
- **End-of-line detection** (`EOL`) and **end-of-file detection** (`EOF`).

## Implementation Details
The lexer processes the input character by character and generates tokens according to predefined rules. It follows these steps:
1. **Skip whitespace** (except newlines, which are returned as `EOL` tokens).
2. **Identify numbers**: It recognizes integers and floating-point numbers.
3. **Recognize identifiers**: Names of variables and functions.
4. **Detect operators**: Arithmetic operators and assignment.
5. **Handle parentheses**.
6. **Raise an error for unrecognized characters**.

## Example Usage
```
Input:  x = sin(3.14) + cos(0) * 2
Output:
Token(IDENTIFIER, x)
Token(ASSIGN, =)
Token(SIN, sin)
Token(LPAREN, ()
Token(FLOAT, 3.14)
Token(RPAREN, ))
Token(PLUS, +)
Token(COS, cos)
Token(LPAREN, ()
Token(INTEGER, 0)
Token(RPAREN, ))
Token(MULTIPLY, *)
Token(INTEGER, 2)
Token(EOF, None)
```

## Conclusion
This lab provided practical experience in lexical analysis, demonstrating how to extract and classify tokens from an input string. The implemented lexer successfully processes arithmetic expressions, handles numerical values, recognizes trigonometric functions, and manages structural elements like parentheses and newlines. Through this exercise, we gained a deeper understanding of character-based parsing, token categorization, and error handling in lexical analysis. Future improvements could include expanding the lexer’s functionality to support additional mathematical functions, variables, and enhanced error detection, making it more robust for complex expression
