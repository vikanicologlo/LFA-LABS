# Laboratory Work No. 5
## Course: Formal Languages & Finite Automata
## Author: Nicologlo Victoria
## Group: FAF-233

### Theory
In formal language theory, Chomsky Normal Form (CNF) is a simplified form of context-free grammars. A grammar in CNF has all of its production rules in one of the following forms:

A → BC (a non-terminal produces two non-terminals)

A → a (a non-terminal produces a single terminal)

S → ε (only if S is the start symbol and doesn't appear on the right side of any rule)

Converting a grammar to CNF is useful for various algorithms, including the CYK parsing algorithm, which requires the grammar to be in this form. The transformation process involves several steps to eliminate different types of problematic productions

## Features

This tool provides the following functionalities:

1. **Define a Grammar:**
   - Specify non-terminals, terminals, production rules, and the start symbol.
   - The grammar is stored as a `Map` for efficient rule lookups, preserving insertion order using `LinkedHashMap`.

2. **Print Grammar Rules:**
   - Outputs the current state of the grammar in a readable format.
   - Non-terminals and their associated productions are displayed.

3. **Check Chomsky Normal Form (CNF):**
   - Verifies whether the current grammar is in CNF.
   - Returns `true` if all production rules comply with CNF constraints:
     - Each production has either one terminal or two non-terminals.
     - No epsilon (ε) productions are allowed except for the start symbol.

4. **Eliminate Epsilon (ε) Productions:**
   - Removes all epsilon productions from the grammar.
   - Handles both direct and indirect nullable non-terminals and ensures all valid expansions of nullable rules are added.

5. **Eliminate Unit Productions:**
   - Removes unit productions (e.g., rules where a non-terminal produces another non-terminal).
   - Replaces unit rules with the productions of the referenced non-terminal.

6. **Eliminate Inaccessible Symbols:**
   - Removes non-terminals and their rules if they cannot be reached from the start symbol.
   - Ensures only reachable grammar rules remain.

7. **Eliminate Non-Productive Symbols:**
   - Removes non-terminals that cannot derive terminal strings.
   - Ensures all remaining rules are productive.

8. **Convert to Chomsky Normal Form (CNF):**
   - Handles complex rules with more than two symbols by introducing new non-terminals.
   - Replaces terminal symbols in mixed rules (e.g., `ABa`) with new non-terminals.
   - Ensures all rules conform to CNF.

## How It Works

The program works by applying a sequence of transformations to the grammar:

1. **Input Grammar:**
   - Define the grammar with non-terminals, terminals, production rules, and a start symbol.

2. **Transformations:**
   - Apply the following transformations step-by-step:
     1. Eliminate epsilon productions.
     2. Eliminate unit productions.
     3. Remove inaccessible symbols.
     4. Remove non-productive symbols.
     5. Convert to CNF by introducing new non-terminals for complex rules.

3. **Output:**
   - The final grammar in CNF is printed in a clean, readable format.

## Conclusion

This tool is an easy-to-use solution for transforming context-free grammars into Chomsky Normal Form. By automating tedious grammar transformations, it allows users to focus on understanding and working with grammars rather than manually performing repetitive tasks. Whether you're a student learning about formal languages or a developer working on parsing algorithms, this tool can save significant time and effort.
