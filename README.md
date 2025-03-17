# Topic: Determinism in Finite Automata. Conversion from NDFA 2 DFA. Chomsky Hierarchy.

### Course: Formal Languages & Finite Automata
### Author: Nicologlo Victoria

----
## Objectives:
* Understanding and implementing finite automata.
* Determining whether a FA is deterministic or non-deterministic.
* Implement conversion of an NDFA to a DFA.

## Theory
Finite Automata and Their Role in Process Representation
A finite automaton is a mathematical model used to represent and analyze various types of processes, particularly in computing and formal language theory. It serves as an abstract machine capable of recognizing patterns and making state transitions based on input symbols. A finite automaton can be compared to a state machine, as both share a similar structure and purpose: they operate through a finite set of states, governed by well-defined transition rules.

The term finite signifies that an automaton has a well-defined structure with a starting state and a set of final (accepting) states. This implies that every process modeled by an automaton has a clear beginning and an end, making it a useful tool for designing systems that require step-by-step decision-making, such as lexical analyzers, control circuits, and artificial intelligence models.

Finite automata can be categorized into two main types: deterministic finite automata (DFA) and non-deterministic finite automata (NFA). The difference between these lies in how they process input symbols and transition between states.

In a deterministic finite automaton (DFA), for every state and input symbol, there exists a single, unique transition to another state. This makes the automaton predictable and easy to implement in hardware or software systems.
In contrast, a non-deterministic finite automaton (NFA) allows multiple possible transitions for a single state and input symbol. This introduces an element of uncertainty, where the system can be in multiple states at the same time, requiring additional processing to determine whether a given input string is accepted.
Non-determinism often arises in real-world scenarios where multiple possible outcomes exist for the same event. In systems theory, determinism refers to the degree to which a system's behavior can be predicted given its initial state and input. A completely deterministic system follows strict rules without randomness, whereas a non-deterministic system introduces some level of unpredictability. If random variables influence transitions, the system can even become stochastic, meaning it follows probabilistic rather than deterministic behavior.

Although non-determinism can be more expressive and flexible, deterministic automata are easier to implement in computational systems. Fortunately, every NFA can be converted into an equivalent DFA using algorithms such as the subset construction algorithm. This transformation involves creating new states in the DFA that represent sets of NFA states, ensuring that for every input symbol, there is a unique transition for each state.

## Implementation description
This Java implementation provides tools for working with formal grammars and finite automata, enabling classification, conversion, and analysis. The code is structured around two core classes, **Grammar** and **FiniteAutomata**, along with a helper class **TransitionKey** to model state-symbol pairs in transitions. Below is a detailed description of its components and functionality.  

The **TransitionKey** class represents a transition key (state, symbol) in finite automata. It overrides equality, hash code, and comparison methods to ensure proper behavior in hash-based collections. This allows transitions like δ(q0, a) → {q1} to be stored and retrieved correctly in maps.  

The **Grammar** class models formal grammars. It includes methods to classify grammars into Chomsky hierarchy types. The classification logic checks production rules for structural patterns: right/left-linear forms for regular grammars (Type 3), single non-terminal LHS for context-free (Type 2), and non-shrinking rules (except for start symbol ε) for context-sensitive (Type 1). The `generate_valid_string` method iteratively expands non-terminals using random productions until only terminals remain, generating valid strings. The `to_finite_automata` method converts a right-linear grammar to an NFA by mapping non-terminals to states and productions to transitions. The `print_grammar` method ensures rules are displayed with the start symbol first, followed by others alphabetically, using a custom comparator for sorting keys.  

The **FiniteAutomata** class handles both NFAs and DFAs. The `string_validation` method simulates automaton execution by tracking active states as the input is processed, accepting the string if any final state is reached. The `is_deterministic` method checks if every state-symbol pair has at most one transition. For NFAs, `convert_to_dfa` applies the subset construction algorithm: it creates new DFA states as sets of NFA states, computes transitions for all symbols, and marks states containing original final states as final. The `convert_to_grammar` method generates a right-linear grammar where transitions like δ(q, a) = p become productions q → a p.  

In the **main** method, two examples are demonstrated. First, a grammar with non-terminals S, B, C, D is classified, showing how the `classify_grammar` method identifies its type. Second, an NFA with states q0–q3 is defined. The code checks if it is deterministic, converts it to a DFA if not, and prints the resulting regular grammar. The NFA’s transitions include non-deterministic choices (e.g., q0 on "a" transitions to q0 and q1), necessitating subset construction. The resulting DFA’s states are sets like [q0], [q0, q1], etc., with transitions derived from the NFA’s behavior.  

For example, the NFA-to-DFA conversion produces a grammar where the start symbol q0’s rules appear first. The `print_grammar` method ensures this by sorting productions, prioritizing the start symbol. The output shows productions like q0 → aq0 and q0 → aq1, reflecting the NFA’s transitions.  

The code emphasizes clarity through sorted transitions and productions, explicit error checks (e.g., empty productions in grammars), and algorithms like subset construction for automata conversions. It serves as a foundational tool for theoretical computer science tasks, enabling experimentation with grammars, automata, and their equivalences.

## Conclusions
This code illustrates core concepts in formal language theory and automata, enabling practical exploration through bidirectional conversions between grammars and finite automata. It classifies grammars into Chomsky hierarchy types by analyzing production rules, generates valid strings via iterative non-terminal replacements, and converts right-linear grammars to NFAs (and vice versa). For automata, it validates input strings through state transitions, checks determinism, and transforms NFAs to DFAs using subset construction—representing DFA states as sets of NFA states. The `main` method demonstrates a non-deterministic automaton with transitions like `q0` on `a` leading to `q0` and `q1`, which is converted into an equivalent DFA and further into a regular grammar. By prioritizing the start symbol in output and sorting transitions systematically, the code emphasizes readability. Overall, it serves as an educational tool for understanding the equivalence between regular grammars and finite automata, while providing a hands-on framework for experimenting with language recognition, non-determinism, and structural transformations.
## References
1. Lecture notes
