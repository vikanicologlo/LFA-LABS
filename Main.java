import java.util.*;

class Grammar {
    private Set<Character> Vn;
    private Set<Character> Vt;
    private Map<Character, List<String>> P;
    private Character S;

    public Grammar(Set<Character> Vn, Set<Character> Vt, Map<Character, List<String>> P, Character S) {
        this.Vn = Vn;
        this.Vt = Vt;
        this.P = P;
        this.S = S;
    }


    public String generateString() {
        return generateString(S);
    }

    private String generateString(Character symbol) {
        if (Vt.contains(symbol)) {
            return symbol.toString();
        }

        List<String> productions = P.get(symbol);
        if (productions == null || productions.isEmpty()) {
            throw new IllegalStateException("No productions for symbol: " + symbol);
        }

        String production = productions.get(new Random().nextInt(productions.size()));
        StringBuilder result = new StringBuilder();
        for (char c : production.toCharArray()) {
            result.append(generateString(c));
        }
        return result.toString();
    }

    public FiniteAutomaton toFiniteAutomaton() {
        Set<Character> Q = new HashSet<>(Vn);
        Q.add('F');

        Set<Character> Sigma = new HashSet<>(Vt);

        Map<Character, Map<Character, Character>> delta = new HashMap<>();
        for (Character state : Vn) {
            Map<Character, Character> transitions = new HashMap<>();
            for (String production : P.get(state)) {
                if (production.isEmpty()) {
                    throw new IllegalStateException("Production cannot be empty.");
                }
                char firstSymbol = production.charAt(0);
                if (Vt.contains(firstSymbol)) {
                    if (production.length() == 1) {
                        transitions.put(firstSymbol, 'F');
                    } else if (production.length() > 1 && Vn.contains(production.charAt(1))) {
                        transitions.put(firstSymbol, production.charAt(1));
                    } else {
                        throw new IllegalStateException("Invalid production: " + production);
                    }
                } else {
                    throw new IllegalStateException("First symbol of production must be a terminal.");
                }
            }
            delta.put(state, transitions);
        }


        delta.put('F', new HashMap<>());

        Character q0 = S;
        Set<Character> F = new HashSet<>();
        F.add('F');

        return new FiniteAutomaton(Q, Sigma, delta, q0, F);
    }
}

class FiniteAutomaton {
    private Set<Character> Q;
    private Set<Character> Sigma;
    private Map<Character, Map<Character, Character>> delta;
    private Character q0;
    private Set<Character> F;

    public FiniteAutomaton(Set<Character> Q, Set<Character> Sigma, Map<Character, Map<Character, Character>> delta, Character q0, Set<Character> F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.delta = delta;
        this.q0 = q0;
        this.F = F;
    }


    public boolean stringBelongToLanguage(String inputString) {
        if (inputString == null) {
            throw new IllegalArgumentException("Input string cannot be null.");
        }

        Character currentState = q0;

        for (char symbol : inputString.toCharArray()) {
            if (!Sigma.contains(symbol)) {
                return false;
            }

            Map<Character, Character> transitions = delta.get(currentState);
            if (transitions == null || !transitions.containsKey(symbol)) {
                return false;
            }

            currentState = transitions.get(symbol);
        }

        return F.contains(currentState);
    }
    }
public class Main {
    public static void main(String[] args) {

        Set<Character> Vn = new HashSet<>(Arrays.asList('S', 'B', 'C', 'D'));
        Set<Character> Vt = new HashSet<>(Arrays.asList('a', 'b', 'c'));
        Map<Character, List<String>> P = new HashMap<>();
        P.put('S', Arrays.asList("aB"));
        P.put('B', Arrays.asList("bS", "aC", "b"));
        P.put('C', Arrays.asList("bD"));
        P.put('D', Arrays.asList("a", "bC", "cS"));

        Grammar grammar = new Grammar(Vn, Vt, P, 'S');


        FiniteAutomaton automaton = grammar.toFiniteAutomaton();
        System.out.println("Generated strings and their validity:");
        for (int i = 0; i < 5; i++) {
            String generatedString = grammar.generateString();
            boolean isValid = automaton.stringBelongToLanguage(generatedString);
            System.out.println("'" + generatedString + "' is valid: " + isValid);
        }
    }
}