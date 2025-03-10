Topic: Intro to formal languages. Regular grammars. Finite Automata.

Course: Formal Languages & Finite Automata
 Author: Nicologlo Victoria


Theory
Grammar is a collection of production rules that specify how a language's valid strings can be created. Production rules, which explain changes from one form to another, variables (non-terminals), terminal symbols, and a start symbol make up a grammar.

Finite Automaton: A computational model for processing strings and identifying patterns. It is composed of an initial state, a collection of accepting states, transitions between states based on input symbols, and states. Finite automata are frequently employed in pattern matching, lexical analysis, and text processing.

Objectives:
* Understanding formal languages and finite automata.
* Implementing the grammar and finite automata.
* Generating and validating strings based on the grammar.

## Implementation

# 1. Class Grammar

This class represents a formal grammar consisting of:
* Non-terminals (nonTerminals): symbols that can be replaced.
* Terminals (terminals): fixed symbols forming the final output.
* Production rules (productions): mappings between non-terminals and possible replacements.
* Start symbol (startSymbol): the initial non-terminal from which string generation begins.

 - generateString()
   
 This method randomly generates a string based on the given grammar:
* Creates a queue and adds the start symbol.
* Iteratively replaces non-terminals using randomly chosen production rules.
* Appends terminal symbols to the final string.
* Stops generation when only terminals remain in the queue or a maximum iteration limit is reached.

 - toFiniteAutomaton()

This method converts the grammar into a finite automaton:
* Automaton states are formed from non-terminals.
* Transitions are determined by production rules.
* The start symbol becomes the automaton's initial state.
* Final states are non-terminals that can produce only terminal strings.

# 2. Class FiniteAutomaton

This class implements a deterministic finite automaton (DFA) based on the grammar:
* States (states): a set of automaton states.
* Alphabet (alphabet): a set of terminal symbols.
* Transitions (transitions): a transition table mapping states and input symbols to new states.
* Start state (startState): the initial state of the automaton.
* Final states (finalStates): states where the automaton accepts a string.

 - stringBelongToLanguage(String inputString)

This method checks whether a string belongs to the automaton's language:
* Starts from the initial state.
* Moves between states according to input symbols.
If a transition does not exist, the string is rejected.
If the automaton ends in a final state, the string is accepted.

# 3. Class Main

The main program class:
* Defines the grammar with non-terminals, terminals, and production rules.
* Converts the grammar into a finite automaton.
* Generates 5 random strings based on the grammar.
* Checks whether each generated string belongs to the automaton's language.
* Prints the generated strings and their validation results.




## Conclusions
In this laboratory, I've successfully implemented given grammar to create finite automata, which demonstrated the practical relationship between formal language theory and computational models. This project highlighted the main principles of automata theory.

## References
1. Lecture notes
