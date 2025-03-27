# Laboratory Work #3: Lexer & Scanner
# Course: Formal Languages & Finite Automata
# Author: Isacescu Maxim, FAF-231
# Professors: Cretu Dumitru, Irina Cojuhari

## Overview
The term **lexer** comes from lexical analysis, which represents the process of extracting lexical tokens from a string of characters. There are several alternative names for a lexer, such as **tokenizer** or **scanner**. Lexical analysis is one of the first stages used in a compiler or interpreter when dealing with programming, markup, or other types of languages.

Tokens are identified based on predefined rules of the language, and the output of the lexer is called **lexemes**. The lexer produces a stream of lexemes, which differ from tokens. A **lexeme** is the raw substring extracted from the input, whereas a **token** provides a category or type to each lexeme. Tokens do not necessarily retain the actual lexeme value but rather classify it with metadata.

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

## Notes
Due to the high number of students implementing a simple calculator lexer, additional requirements were introduced. The lexer should support:
- **Integers and floating-point numbers**.
- **Trigonometric operations** (`sin` and `cos`).

The goal is to extend the basic lexer functionality beyond a simple calculator and explore more advanced tokenization techniques.

## License
This project is open-source and can be modified or extended as needed.

