import java.util.*;

class Grammar {
    private Set<Character> VN;
    private Set<Character> VT;
    private Map<Character, List<String>> P;
    private char S;

    public Grammar(Set<Character> VN, Set<Character> VT, Map<Character, List<String>> P, char S) {
        this.VN = VN;
        this.VT = VT;
        this.P = P;
        this.S = S;
    }

    public String generateString() {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        Queue<Character> queue = new LinkedList<>();
        queue.add(S);

        while (!queue.isEmpty()) {
            char current = queue.poll();
            if (VT.contains(current)) {
                result.append(current);
            } else if (P.containsKey(current)) {
                List<String> rules = P.get(current);
                String chosenRule = rules.get(random.nextInt(rules.size()));
                for (char c : chosenRule.toCharArray()) {
                    queue.add(c);
                }
            }
        }
        return result.toString();
    }

    public FiniteAutomaton toFiniteAutomaton() {
        Set<String> states = new HashSet<>();
        Set<Character> alphabet = new HashSet<>(VT);
        Map<String, Map<Character, String>> transitions = new HashMap<>();
        String startState = "S";
        Set<String> finalStates = new HashSet<>();

        for (Character nonTerminal : P.keySet()) {
            states.add(nonTerminal.toString());
            transitions.put(nonTerminal.toString(), new HashMap<>());
            for (String rule : P.get(nonTerminal)) {
                if (rule.length() == 1 && VT.contains(rule.charAt(0))) {
                    finalStates.add(nonTerminal.toString());
                } else {
                    char terminal = rule.charAt(0);
                    String nextState = rule.length() > 1 ? String.valueOf(rule.charAt(1)) : "";
                    transitions.get(nonTerminal.toString()).put(terminal, nextState);
                }
            }
        }

        return new FiniteAutomaton(states, alphabet, transitions, startState, finalStates);
    }
}

class FiniteAutomaton {
    private Set<String> states;
    private Set<Character> alphabet;
    private Map<String, Map<Character, String>> transitions;
    private String startState;
    private Set<String> finalStates;

    public FiniteAutomaton(Set<String> states, Set<Character> alphabet, Map<String, Map<Character, String>> transitions,
                           String startState, Set<String> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.startState = startState;
        this.finalStates = finalStates;
    }

    public boolean stringBelongToLanguage(String inputString) {
        String currentState = startState;
        for (char symbol : inputString.toCharArray()) {
            if (!transitions.containsKey(currentState) || !transitions.get(currentState).containsKey(symbol)) {
                return false;
            }
            currentState = transitions.get(currentState).get(symbol);
        }
        return finalStates.contains(currentState);
    }
}

public class Main {
    public static void main(String[] args) {
        Set<Character> VN = new HashSet<>(Arrays.asList('S', 'B', 'C', 'D'));
        Set<Character> VT = new HashSet<>(Arrays.asList('a', 'b', 'c'));
        Map<Character, List<String>> P = new HashMap<>();

        P.put('S', Arrays.asList("aB"));
        P.put('B', Arrays.asList("bS", "aC", "b"));
        P.put('C', Arrays.asList("bD"));
        P.put('D', Arrays.asList("a", "bC", "cS"));

        Grammar grammar = new Grammar(VN, VT, P, 'S');

        System.out.println("Generated strings:");
        for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateString());
        }

        FiniteAutomaton fa = grammar.toFiniteAutomaton();

        System.out.println("Checking if strings belong to the language:");
        String[] testStrings = {"ab", "abc", "b", "aab", "bb"};
        for (String str : testStrings) {
            System.out.println(str + " -> " + fa.stringBelongToLanguage(str));
        }
    }
}
