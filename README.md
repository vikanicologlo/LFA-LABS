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

## Implementation

This tool is designed to work with formal grammars and helps automate several tasks that are essential in grammar transformations.

First, you can define your grammar by specifying the non-terminals, terminals, production rules, and the start symbol. The grammar is stored efficiently in a LinkedHashMap, which helps with quick lookups while preserving the order in which the rules are added.

Once your grammar is defined, you can print it in a clear and readable format, displaying the non-terminals and their corresponding production rules.

The tool also checks if your grammar is in Chomsky Normal Form (CNF). CNF requires that each production rule has either a single terminal or two non-terminals, and no epsilon (ε) productions should exist, except for the start symbol. If your grammar doesn't meet these conditions, the tool will help transform it.

One of the core transformations the tool performs is eliminating epsilon (ε) productions. This means that it removes any production rules that derive the empty string, handling both direct and indirect nullable non-terminals. All valid expansions of nullable rules are also taken into account.

The tool also helps remove unit productions—rules where a non-terminal directly produces another non-terminal. These unit productions are replaced by the actual productions of the referenced non-terminal, simplifying the grammar.

Another important step is removing inaccessible symbols. These are non-terminals and production rules that can't be reached from the start symbol. The tool ensures only the relevant rules remain, making the grammar easier to work with.

Next, the tool eliminates non-productive symbols. These are non-terminals that cannot generate terminal strings and are therefore removed. This ensures that only productive rules are left.

Finally, the tool helps convert your grammar into Chomsky Normal Form (CNF). For rules that are more complex, such as those with more than two symbols, new non-terminals are introduced to break them down into simpler parts. Mixed rules, like those that include both terminals and non-terminals (e.g., ABa), are also adjusted by replacing terminals with new non-terminals, ensuring the grammar meets CNF requirements.

Once all these transformations are applied, the tool outputs the final grammar in CNF in a clear, human-readable format, so you can see the end result of the changes.


## Conclusion

This tool is an easy-to-use solution for transforming context-free grammars into Chomsky Normal Form. By automating tedious grammar transformations, it allows users to focus on understanding and working with grammars rather than manually performing repetitive tasks. Whether you're a student learning about formal languages or a developer working on parsing algorithms, this tool can save significant time and effort.
