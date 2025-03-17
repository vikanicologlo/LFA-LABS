import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static class TransitionKey implements Comparable<TransitionKey> {
        public String state;
        public String symbol;

        public TransitionKey(String state, String symbol) {
            this.state = state;
            this.symbol = symbol;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TransitionKey)) return false;
            TransitionKey that = (TransitionKey) o;
            return Objects.equals(state, that.state) && Objects.equals(symbol, that.symbol);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, symbol);
        }

        @Override
        public int compareTo(TransitionKey other) {
            int cmp = this.state.compareTo(other.state);
            if (cmp != 0) {
                return cmp;
            }
            return this.symbol.compareTo(other.symbol);
        }

        @Override
        public String toString() {
            return "(" + state + ", " + symbol + ")";
        }
    }

    public static class Grammar {
        public Set<String> VN;
        public Set<String> VT;
        public Map<String, List<String>> P;
        public String start_symbol;

        public Grammar(Set<String> VN, Set<String> VT, Map<String, List<String>> P) {
            this(VN, VT, P, "S");
        }

        public Grammar(Set<String> VN, Set<String> VT, Map<String, List<String>> P, String start_symbol) {
            this.VN = VN;
            this.VT = VT;
            this.P = P;
            this.start_symbol = start_symbol;
        }

        public String classify_grammar() {
            boolean is_type_3_right = true;  // Right-linear Regular
            boolean is_type_3_left = true;   // Left-linear Regular
            boolean is_type_2 = true;        // Context-Free
            boolean is_type_1 = true;        // Context-Sensitive


            boolean has_empty_production = false;
            for (Map.Entry<String, List<String>> entry : P.entrySet()) {
                String lhs = entry.getKey();
                List<String> rules = entry.getValue();
                for (String rule : rules) {
                    if (rule.isEmpty()) {
                        if (!lhs.equals(start_symbol) || has_empty_production) {
                            is_type_1 = false;
                        }
                        has_empty_production = true;
                    }
                }
            }

            for (Map.Entry<String, List<String>> entry : P.entrySet()) {
                String lhs = entry.getKey();
                List<String> rules = entry.getValue();
                if (lhs.length() != 1 || !VN.contains(lhs)) {
                    is_type_2 = is_type_3_right = is_type_3_left = false;
                }

                for (String rule : rules) {
                    if (rule.isEmpty()) {
                        is_type_3_right = is_type_3_left = false;
                        continue;
                    }

                    if (rule.length() > 1) {
                        String allButLast = rule.substring(0, rule.length() - 1);
                        for (int i = 0; i < allButLast.length(); i++) {
                            String c = String.valueOf(allButLast.charAt(i));
                            if (VN.contains(c)) {
                                is_type_3_right = false;
                                break;
                            }
                        }
                    }

                    if (rule.length() > 1) {
                        String first = rule.substring(0, 1);
                        String rest = rule.substring(1);
                        if (!VN.contains(first)) {
                            is_type_3_left = false;
                        }
                        for (int i = 0; i < rest.length(); i++) {
                            String c = String.valueOf(rest.charAt(i));
                            if (VN.contains(c)) {
                                is_type_3_left = false;
                                break;
                            }
                        }
                    }
                    if (rule.length() < lhs.length() && !(lhs.equals(start_symbol) && rule.isEmpty())) {
                        is_type_1 = false;
                    }
                }
            }

            if (is_type_3_right || is_type_3_left) {
                return "Type 3: Regular Grammar";
            }
            if (is_type_2) {
                return "Type 2: Context-Free Grammar";
            }
            if (is_type_1) {
                return "Type 1: Context-Sensitive Grammar";
            }
            return "Type 0: Unrestricted Grammar";
        }

        public String generate_valid_string() {
            String current = start_symbol;
            Random random = new Random();
            while (containsVN(current)) {
                String new_string = "";
                for (int i = 0; i < current.length(); i++) {
                    String symbol = String.valueOf(current.charAt(i));
                    if (VN.contains(symbol)) {
                        List<String> productions = P.get(symbol);
                        new_string += productions.get(random.nextInt(productions.size()));
                    } else {
                        new_string += symbol;
                    }
                }
                current = new_string;
            }
            return current;
        }

        private boolean containsVN(String str) {
            for (int i = 0; i < str.length(); i++) {
                String c = String.valueOf(str.charAt(i));
                if (VN.contains(c)) {
                    return true;
                }
            }
            return false;
        }

        public FiniteAutomata to_finite_automata() {
            Set<String> states = new HashSet<>(VN);
            states.add("");
            Set<String> alphabet = new HashSet<>(VT);
            Map<TransitionKey, Set<String>> transitions = new HashMap<>();
            String start_state = start_symbol;
            Set<String> final_states = new HashSet<>();
            final_states.add("");

            for (Map.Entry<String, List<String>> entry : P.entrySet()) {
                String non_terminal = entry.getKey();
                List<String> rules = entry.getValue();
                for (String rule : rules) {
                    if (rule.length() == 1 && VT.contains(rule)) {
                        TransitionKey key = new TransitionKey(non_terminal, rule);
                        transitions.putIfAbsent(key, new HashSet<>());
                        transitions.get(key).add("");
                    } else if (!rule.isEmpty()) {
                        String first_symbol = rule.substring(0, 1);
                        String next_state = rule.length() > 1 ? rule.substring(1) : "";
                        TransitionKey key = new TransitionKey(non_terminal, first_symbol);
                        transitions.putIfAbsent(key, new HashSet<>());
                        transitions.get(key).add(next_state);
                    }
                }
            }
            return new FiniteAutomata(states, alphabet, transitions, start_state, final_states);
        }

        public void print_grammar() {
            String vn_str = "Non-terminals = {" + VN.stream().map(s -> "'" + s + "'").collect(Collectors.joining(", ")) + "}";
            String vt_str = "Terminals = {" + VT.stream().map(s -> "'" + s + "'").collect(Collectors.joining(", ")) + "}";

            StringBuilder p_str = new StringBuilder("Productions = {\n");
            List<String> productionsKeys = new ArrayList<>(P.keySet());

            Comparator<String> comparator = Comparator
                    .comparing((String key) -> !key.equals(start_symbol))
                    .thenComparing(Comparator.naturalOrder());
            productionsKeys.sort(comparator);

            for (String lhs : productionsKeys) {
                List<String> rules = P.get(lhs);
                String rules_str = rules.stream()
                        .map(rule -> rule.isEmpty() ? "ε" : rule)
                        .collect(Collectors.joining(", "));
                p_str.append("    ").append(lhs).append(" → ").append(rules_str).append(",\n");
            }
            p_str.append("}");

            String start_str = "Start Symbol = " + start_symbol;
            String grammar_str = vn_str + "\n" + vt_str + "\n" + p_str + "\n" + start_str;
            System.out.println(grammar_str);
        }
    }

    public static class FiniteAutomata {
        public Set<String> states;
        public Set<String> alphabet;
        public Map<TransitionKey, Set<String>> transitions;
        public String start_state;
        public Set<String> final_states;

        public Map<TransitionKey, Set<String>> full_transitions;

        public FiniteAutomata(Set<String> states, Set<String> alphabet, Map<TransitionKey, Set<String>> transitions, String start_state, Set<String> final_states) {
            this.states = states;
            this.alphabet = alphabet;
            this.transitions = transitions;
            this.start_state = start_state;
            this.final_states = final_states;
            this.full_transitions = new HashMap<>();
            for (Map.Entry<TransitionKey, Set<String>> entry : transitions.entrySet()) {
                this.full_transitions.put(entry.getKey(), new HashSet<>(entry.getValue()));
            }
        }

        public boolean string_validation(String input_string) {
            Set<String> current_states = new HashSet<>();
            current_states.add(this.start_state);

            for (int i = 0; i < input_string.length(); i++) {
                String symbol = String.valueOf(input_string.charAt(i));
                if (!alphabet.contains(symbol)) {
                    return false;
                }
                Set<String> next_states = new HashSet<>();
                for (String state : current_states) {
                    TransitionKey key = new TransitionKey(state, symbol);
                    if (full_transitions.containsKey(key)) {
                        next_states.addAll(full_transitions.get(key));
                    }
                }
                if (next_states.isEmpty()) {
                    return false;
                }
                current_states = next_states;
            }
            for (String state : current_states) {
                if (final_states.contains(state)) {
                    return true;
                }
            }
            return false;
        }

        public boolean is_deterministic() {
            for (String state : states) {
                for (String symbol : alphabet) {
                    TransitionKey key = new TransitionKey(state, symbol);
                    if (state != null && !isComposite(state)) {
                        if (!transitions.containsKey(key)) {
                            continue;
                        }
                        if (transitions.get(key).size() > 1) {
                            return false;
                        }
                    } else {
                        if (!transitions.containsKey(key)) {
                            continue;
                        }
                        if (transitions.get(key).size() != 1) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        private boolean isComposite(String state) {
            return state.startsWith("[") || state.contains(", ");
        }

        public FiniteAutomata convert_to_dfa() {
            Set<Set<String>> dfa_states = new HashSet<>();
            Map<TransitionKey, Set<Set<String>>> dfa_transitions = new HashMap<>();
            Set<Set<String>> dfa_final_states = new HashSet<>();

            SortedSet<String> start_state_set = new TreeSet<>();
            start_state_set.add(this.start_state);
            Set<String> start_state_stringSet = new HashSet<>(start_state_set);

            List<Set<String>> unmarked_states = new LinkedList<>();
            unmarked_states.add(start_state_stringSet);
            dfa_states.add(start_state_stringSet);

            while (!unmarked_states.isEmpty()) {
                Set<String> current_state_set = unmarked_states.removeFirst();

                for (String state : current_state_set) {
                    if (final_states.contains(state)) {
                        dfa_final_states.add(current_state_set);
                        break;
                    }
                }

                for (String symbol : alphabet) {
                    Set<String> next_state_set = new HashSet<>();
                    for (String state : current_state_set) {
                        TransitionKey key = new TransitionKey(state, symbol);
                        if (full_transitions.containsKey(key)) {
                            next_state_set.addAll(full_transitions.get(key));
                        }
                    }
                    if (next_state_set.isEmpty()) {
                        continue;
                    }

                    TransitionKey dfaKey = new TransitionKey(stateSetToString(current_state_set), symbol);
                    dfa_transitions.put(dfaKey, new HashSet<>(Arrays.asList(next_state_set)));

                    if (!dfa_states.contains(next_state_set)) {
                        dfa_states.add(next_state_set);
                        unmarked_states.add(next_state_set);
                    }
                }
            }

            Map<TransitionKey, Set<String>> new_transitions = new HashMap<>();
            for (Map.Entry<TransitionKey, Set<Set<String>>> entry : dfa_transitions.entrySet()) {
                TransitionKey key = entry.getKey();
                Set<Set<String>> valueSet = entry.getValue();
                Set<String> nextSet = valueSet.iterator().next();
                String nextStateStr = stateSetToString(nextSet);
                TransitionKey newKey = new TransitionKey(key.state, key.symbol);
                new_transitions.put(newKey, new HashSet<>(Arrays.asList(nextStateStr)));
            }

            Set<String> new_states = new HashSet<>();
            for (Set<String> stateSet : dfa_states) {
                new_states.add(stateSetToString(stateSet));
            }

            String new_start_state = stateSetToString(start_state_stringSet);
            Set<String> new_final_states = new HashSet<>();
            for (Set<String> stateSet : dfa_final_states) {
                new_final_states.add(stateSetToString(stateSet));
            }

            return new FiniteAutomata(new_states, this.alphabet, new_transitions, new_start_state, new_final_states);
        }

        private String stateSetToString(Set<String> stateSet) {
            List<String> list = new ArrayList<>(stateSet);
            Collections.sort(list);
            return list.toString();
        }

        public Grammar convert_to_grammar() {
            Set<String> Vn = new HashSet<>(this.alphabet);
            Set<String> Vt = new HashSet<>(this.states);
            String S = this.start_state;
            Map<String, List<String>> P = new HashMap<>();

            for (Map.Entry<TransitionKey, Set<String>> entry : this.transitions.entrySet()) {
                TransitionKey key = entry.getKey();
                Set<String> values = entry.getValue();
                for (String value : values) {
                    if (!P.containsKey(key.state)) {
                        List<String> prodList = new ArrayList<>();
                        prodList.add(key.symbol + value);
                        P.put(key.state, prodList);
                    } else {
                        if (final_states.contains(key.state)) {
                            P.get(key.state).add(key.symbol);
                        }
                        P.get(key.state).add(key.symbol + value);
                    }
                }
            }
            return new Grammar(Vn, Vt, P, S);
        }

        public void print_transitions() {
            System.out.println("Transitions:");
            // To sort the keys, put them in a list and sort using their compareTo method.
            List<TransitionKey> keys = new ArrayList<>(transitions.keySet());
            Collections.sort(keys);
            for (TransitionKey key : keys) {
                System.out.println("  δ" + key.toString() + " = " + transitions.get(key));
            }
        }
    }

    public static void main(String[] args) {
        // lab1
        Set<String> VN = new HashSet<>(Arrays.asList("S", "B", "C", "D"));
        Set<String> VT = new HashSet<>(Arrays.asList("a", "b", "c"));
        Map<String, List<String>> P = new HashMap<>();
        P.put("S", Arrays.asList("aB"));
        P.put("A", Arrays.asList("bS", "aC","b"));
        P.put("B", Arrays.asList("bD"));
        P.put("C", Arrays.asList("a", "bC", "cS"));


        Grammar grammar = new Grammar(VN, VT, P);
        System.out.println(grammar.classify_grammar());

        // lab2
        Set<String> Q = new HashSet<>(Arrays.asList("q0", "q1", "q2", "q3"));
        Set<String> Sigma = new HashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> F = new HashSet<>(Arrays.asList("q3"));
        Map<TransitionKey, Set<String>> delta = new HashMap<>();
        delta.put(new TransitionKey("q0", "a"), new HashSet<>(Arrays.asList("q0", "q1")));
        delta.put(new TransitionKey("q1", "b"), new HashSet<>(Arrays.asList("q2")));
        delta.put(new TransitionKey("q2", "c"), new HashSet<>(Arrays.asList("q3")));
        delta.put(new TransitionKey("q3", "c"), new HashSet<>(Arrays.asList("q3")));
        delta.put(new TransitionKey("q2", "a"), new HashSet<>(Arrays.asList("q2")));

        FiniteAutomata fa = new FiniteAutomata(Q, Sigma, delta, "q0", F);
        System.out.println("DFA: " + fa.is_deterministic());

        if (!fa.is_deterministic()) {
            FiniteAutomata dfa = fa.convert_to_dfa();
            System.out.println("\nTo DFA: " + dfa.is_deterministic());
        }

        System.out.println("\nTo Regular Grammar:");
        Grammar rg = fa.convert_to_grammar();
        rg.print_grammar();
    }
}
