import java.util.*;

class FiniteAutomaton {
    private Set<String> states;
    private Set<Character> alphabet;
    private Map<String, Map<Character, Set<String>>> transitions;
    private String startState;
    private Set<String> finalStates;

    public FiniteAutomaton(Set<String> states, Set<Character> alphabet, Map<String, Map<Character, Set<String>>> transitions, String startState, Set<String> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.startState = startState;
        this.finalStates = finalStates;
    }

    public boolean isDeterministic() {
        for (String state : transitions.keySet()) {
            for (Character symbol : transitions.get(state).keySet()) {
                if (transitions.get(state).get(symbol).size() > 1) {
                    return false; // More than one transition for the same symbol from a state
                }
            }
        }
        return true;
    }

    public FiniteAutomaton convertToDFA() {
        Map<Set<String>, String> stateMapping = new HashMap<>();
        Queue<Set<String>> queue = new LinkedList<>();
        Set<String> newStates = new HashSet<>();
        Map<String, Map<Character, String>> newTransitions = new HashMap<>();
        Set<String> newFinalStates = new HashSet<>();

        Set<String> startSet = epsilonClosure(Collections.singleton(startState));
        queue.add(startSet);
        stateMapping.put(startSet, startSet.toString());
        newStates.add(startSet.toString());

        while (!queue.isEmpty()) {
            Set<String> currentSet = queue.poll();
            String newStateName = stateMapping.get(currentSet);
            newTransitions.putIfAbsent(newStateName, new HashMap<>());

            for (Character symbol : alphabet) {
                Set<String> nextSet = new HashSet<>();
                for (String state : currentSet) {
                    nextSet.addAll(transitions.getOrDefault(state, new HashMap<>()).getOrDefault(symbol, Collections.emptySet()));
                }
                nextSet = epsilonClosure(nextSet);

                if (!nextSet.isEmpty()) {
                    if (!stateMapping.containsKey(nextSet)) {
                        stateMapping.put(nextSet, nextSet.toString());
                        queue.add(nextSet);
                        newStates.add(nextSet.toString());
                    }
                    newTransitions.get(newStateName).put(symbol, stateMapping.get(nextSet));
                }
            }
        }

        for (Set<String> stateSet : stateMapping.keySet()) {
            for (String state : stateSet) {
                if (finalStates.contains(state)) {
                    newFinalStates.add(stateMapping.get(stateSet));
                    break;
                }
            }
        }

        return new FiniteAutomaton(newStates, alphabet, convertTransitions(newTransitions), stateMapping.get(startSet), newFinalStates);
    }

    private Set<String> epsilonClosure(Set<String> states) {
        return states; // This implementation assumes no epsilon transitions
    }

    private Map<String, Map<Character, Set<String>>> convertTransitions(Map<String, Map<Character, String>> transitions) {
        Map<String, Map<Character, Set<String>>> result = new HashMap<>();
        for (String state : transitions.keySet()) {
            result.put(state, new HashMap<>());
            for (Character symbol : transitions.get(state).keySet()) {
                result.get(state).put(symbol, new HashSet<>(Collections.singleton(transitions.get(state).get(symbol))));
            }
        }
        return result;
    }

    public void printAutomaton() {
        System.out.println("States: " + states);
        System.out.println("Alphabet: " + alphabet);
        System.out.println("Start State: " + startState);
        System.out.println("Final States: " + finalStates);
        System.out.println("Transitions:");
        for (String state : transitions.keySet()) {
            for (Character symbol : transitions.get(state).keySet()) {
                System.out.println("  " + state + " --" + symbol + "--> " + transitions.get(state).get(symbol));
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Set<String> states = new HashSet<>(Arrays.asList("q0", "q1", "q2", "q3"));
        Set<Character> alphabet = new HashSet<>(Arrays.asList('a', 'b', 'c'));
        Set<String> finalStates = new HashSet<>(Collections.singleton("q3"));
        Map<String, Map<Character, Set<String>>> transitions = new HashMap<>();

        transitions.put("q0", new HashMap<>());
        transitions.get("q0").put('a', new HashSet<>(Arrays.asList("q0", "q1")));
        transitions.put("q1", new HashMap<>());
        transitions.get("q1").put('b', new HashSet<>(Collections.singleton("q2")));
        transitions.put("q2", new HashMap<>());
        transitions.get("q2").put('c', new HashSet<>(Collections.singleton("q3")));
        transitions.get("q2").put('a', new HashSet<>(Collections.singleton("q2")));
        transitions.put("q3", new HashMap<>());
        transitions.get("q3").put('c', new HashSet<>(Collections.singleton("q3")));

        FiniteAutomaton ndfa = new FiniteAutomaton(states, alphabet, transitions, "q0", finalStates);
        System.out.println("Initial NDFA:");
        ndfa.printAutomaton();

        System.out.println("\nChecking Determinism:");
        System.out.println(ndfa.isDeterministic() ? "Deterministic" : "Non-Deterministic");

        System.out.println("\nConverting to DFA:");
        FiniteAutomaton dfa = ndfa.convertToDFA();
        dfa.printAutomaton();
    }
}