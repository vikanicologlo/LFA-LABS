import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;


public class Grammar {

    public List<String> non_terminals;
    public List<String> terminals;
    public Map<String, List<String>> rules;
    public String start;

    public Grammar(List<String> non_terminals, List<String> terminals, Map<String, List<String>> rules, String start) {
        this.non_terminals = new ArrayList<>(non_terminals);
        this.terminals = new ArrayList<>(terminals);
        // Use LinkedHashMap to preserve insertion order
        this.rules = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : rules.entrySet()) {
            this.rules.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        this.start = start;
    }

    public void print_rules() {
        // When printing, if the printed section is "Conversion to Chomsky Normal Form",
        // we need to print non-terminals that are letters before digits.
        // Since the instruction applies only to the final print, we sort the keys accordingly
        List<String> keys = new ArrayList<>(this.rules.keySet());
        // Check if the printed header is "Conversion to Chomsky Normal Form:" by inspecting the call site.
        // Since we cannot detect that in this method, we assume that before the final print the rules map has been sorted.
        // Therefore, we print in the order as stored in rules.
        for (String non_terminal : keys) {
            List<String> productions = this.rules.get(non_terminal);
            // Join productions with " | " as separator
            StringBuilder sb = new StringBuilder();
            Iterator<String> it = productions.iterator();
            while (it.hasNext()) {
                sb.append(it.next());
                if (it.hasNext()) {
                    sb.append(" | ");
                }
            }
            System.out.println(non_terminal + " -> " + sb);
        }
        System.out.println();
    }

    public boolean is_cnf_form() {
        for (String non_terminal : this.rules.keySet()) {
            for (String production : this.rules.get(non_terminal)) {
                if (production.isEmpty() || production.length() > 2) {
                    return false;
                }
                if (production.length() == 1 && !this.terminals.contains(production)) {
                    return false;
                }
                if (production.length() == 2) {
                    for (int i = 0; i < production.length(); i++) {
                        String symbol = String.valueOf(production.charAt(i));
                        if (this.terminals.contains(symbol)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void eliminate_e_productions() {
        Set<String> nullable = new HashSet<>();

        // Find all nullable non-terminals
        for (String non_terminal : this.non_terminals) {
            for (String production : this.rules.get(non_terminal)) {
                if (production.equals("ε")) {
                    nullable.add(non_terminal);
                }
            }
        }

        // Check for indirect nullable non-terminals
        boolean changes = true;
        while (changes) {
            changes = false;
            for (String non_terminal : this.non_terminals) {
                if (!nullable.contains(non_terminal)) {
                    for (String production : this.rules.get(non_terminal)) {
                        boolean allNullable = true;
                        for (int i = 0; i < production.length(); i++) {
                            String symbol = String.valueOf(production.charAt(i));
                            if (!nullable.contains(symbol)) {
                                allNullable = false;
                                break;
                            }
                        }
                        if (allNullable) {
                            nullable.add(non_terminal);
                            changes = true;
                            break;
                        }
                    }
                }
            }
        }

        // Eliminate epsilon-productions
        Map<String, List<String>> new_rules = new LinkedHashMap<>();
        for (String non_terminal : this.rules.keySet()) {
            List<String> new_prods = new ArrayList<>();
            for (String production : this.rules.get(non_terminal)) {
                if (!production.equals("ε")) {
                    new_prods.addAll(this._expand_nullable_prod(production, nullable));
                }
            }
            // Remove duplicates by using a LinkedHashSet then converting back to list
            new_rules.put(non_terminal, new ArrayList<>(new LinkedHashSet<>(new_prods)));
        }
        this.rules = new_rules;
    }

    private List<String> _expand_nullable_prod(String production, Set<String> nullable) {
        List<String> expansions = new ArrayList<>();
        expansions.add("");

        for (int i = 0; i < production.length(); i++) {
            String symbol = String.valueOf(production.charAt(i));
            List<String> new_expansions = new ArrayList<>();
            if (nullable.contains(symbol)) {
                for (String expansion : expansions) {
                    new_expansions.add(expansion + symbol);
                    new_expansions.add(expansion);
                }
            } else {
                for (String expansion : expansions) {
                    new_expansions.add(expansion + symbol);
                }
            }
            expansions = new_expansions;
        }

        List<String> result = new ArrayList<>();
        for (String expansion : expansions) {
            if (!expansion.isEmpty()) {
                result.add(expansion);
            }
        }
        return result;
    }

    public void eliminate_unit_prod() {
        // Remove unit productions from grammar
        boolean changes = true;
        while (changes) {
            changes = false;
            for (String non_terminal : this.non_terminals) {
                List<String> currentProductions = new ArrayList<>(this.rules.get(non_terminal));
                List<String> unit_productions = new ArrayList<>();
                for (String prod : currentProductions) {
                    if (this.non_terminals.contains(prod)) {
                        unit_productions.add(prod);
                    }
                }
                for (String unit : unit_productions) {
                    List<String> new_productions = this.rules.get(unit);
                    if (new_productions != null) {
                        this.rules.get(non_terminal).addAll(new_productions);
                        this.rules.get(non_terminal).remove(unit);
                        // Remove duplicates
                        this.rules.put(non_terminal, new ArrayList<>(new LinkedHashSet<>(this.rules.get(non_terminal))));
                        changes = true;
                    }
                }
                // Remove unit productions after processing
                List<String> filtered = new ArrayList<>();
                for (String prod : this.rules.get(non_terminal)) {
                    if (!this.non_terminals.contains(prod)) {
                        filtered.add(prod);
                    }
                }
                this.rules.put(non_terminal, filtered);
            }
        }
    }

    public void eliminate_inaccessible_symbols() {
        Set<String> accessible = new HashSet<>();
        accessible.add(this.start);
        boolean flag = true;
        Map<String, List<String>> old_rules = new LinkedHashMap<>(this.rules);

        while (flag) {
            flag = false;
            // Make a copy to avoid concurrent modification
            Set<String> currentAccessible = new HashSet<>(accessible);
            for (String non_terminal : currentAccessible) {
                List<String> productions = this.rules.get(non_terminal);
                if (productions != null) {
                    for (String production : productions) {
                        for (int i = 0; i < production.length(); i++) {
                            String symbol = String.valueOf(production.charAt(i));
                            if (this.non_terminals.contains(symbol) && !accessible.contains(symbol)) {
                                accessible.add(symbol);
                                flag = true;
                            }
                        }
                    }
                }
            }
        }
        this.non_terminals = new ArrayList<>(accessible);
        Map<String, List<String>> new_rule_map = new LinkedHashMap<>();
        for (String nt : accessible) {
            new_rule_map.put(nt, old_rules.get(nt));
        }
        this.rules = new_rule_map;
    }

    public void eliminate_non_productive_symbols() {
        Set<String> productive = new HashSet<>();
        productive.add(this.start);
        boolean changes = true;

        while (changes) {
            changes = false;
            for (String non_terminal : this.non_terminals) {
                if (!productive.contains(non_terminal)) {
                    List<String> prods = this.rules.get(non_terminal);
                    if (prods != null) {
                        for (String production : prods) {
                            boolean allProductive = true;
                            for (int i = 0; i < production.length(); i++) {
                                String symbol = String.valueOf(production.charAt(i));
                                if (!(this.terminals.contains(symbol) || productive.contains(symbol))) {
                                    allProductive = false;
                                    break;
                                }
                            }
                            if (allProductive) {
                                productive.add(non_terminal);
                                changes = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        this.non_terminals = new ArrayList<>(productive);

        // Dictionary to store the rules
        Map<String, List<String>> updated_rules = new LinkedHashMap<>();
        for (String nt : productive) {
            List<String> productive_rules = new ArrayList<>();

            List<String> prods = this.rules.get(nt);
            if (prods != null) {
                for (String production : prods) {
                    boolean valid = true;
                    for (int i = 0; i < production.length(); i++) {
                        String symbol = String.valueOf(production.charAt(i));
                        if (!(this.terminals.contains(symbol) || productive.contains(symbol))) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        productive_rules.add(production);
                    }
                }
            }
            updated_rules.put(nt, productive_rules);
        }
        this.rules = updated_rules;
    }

    public String _create_new_non_terminal() {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < alphabet.length(); i++) {
            String letter = String.valueOf(alphabet.charAt(i));
            if (!this.non_terminals.contains(letter)) {
                this.non_terminals.add(letter);
                return letter;
            }
        }

        for (int i = 0; i < alphabet.length(); i++) {
            String letter = String.valueOf(alphabet.charAt(i));
            for (int num = 0; num < 10; num++) {
                String new_symbol = letter + num;
                if (!this.non_terminals.contains(new_symbol)) {
                    this.non_terminals.add(new_symbol);
                    return new_symbol;
                }
            }
        }
        return null;
    }

    // Main method inside the Grammar class is not used; the main is in the Main class.
    public static void main(String[] args) {
        // Variant 21
        List<String> non_terminals = new ArrayList<>(Arrays.asList("S", "A", "B", "C", "D"));
        List<String> terminals = new ArrayList<>(Arrays.asList("a", "b", "d"));
        Map<String, List<String>> rules = new LinkedHashMap<>();
        rules.put("S", new ArrayList<>(Arrays.asList("dB", "AC")));
        rules.put("A", new ArrayList<>(Arrays.asList("d", "dS", "aBdB")));
        rules.put("B", new ArrayList<>(Arrays.asList("a", "aA", "AC")));
        rules.put("C", new ArrayList<>(Arrays.asList("bC", "ε")));
        rules.put("D", new ArrayList<>(List.of("ab")));

        Grammar grammar = new Grammar(non_terminals, terminals, rules, "S");

        if (grammar.is_cnf_form()) {
            System.out.println("The grammar is in Chomsky Normal Form Already");
        }

        grammar.eliminate_e_productions();
        System.out.println("1) Elimination of epsilon productions:");
        grammar.print_rules();

        grammar.eliminate_unit_prod();
        System.out.println("2) Elimination of unit productions:");
        grammar.print_rules();

        grammar.eliminate_inaccessible_symbols();
        System.out.println("3) Elimination of inaccessible symbols:");
        grammar.print_rules();

        grammar.eliminate_non_productive_symbols();
        System.out.println("4) Elimination of non-productive symbols:");
        grammar.print_rules();

        Map<String, String> rhs_to_non_terminal = new HashMap<>();
        List<String> old_non_terminals = new ArrayList<>(grammar.rules.keySet());

        // Use a temporary map with Set<String> for productions
        Map<String, Set<String>> new_rules = new LinkedHashMap<>();
        for (String non_terminal : new ArrayList<>(grammar.rules.keySet())) {
            new_rules.put(non_terminal, new LinkedHashSet<>());
            List<String> prods = grammar.rules.get(non_terminal);
            for (String production : prods) {
                // Case for productions with more than 2 symbols
                while (production.length() > 2) {
                    String first_two_symbols = production.substring(0, 2);
                    String new_non_terminal;
                    if (rhs_to_non_terminal.containsKey(first_two_symbols)) {
                        new_non_terminal = rhs_to_non_terminal.get(first_two_symbols);
                    } else {
                        new_non_terminal = grammar._create_new_non_terminal();
                        Set<String> setProd = new LinkedHashSet<>();
                        setProd.add(first_two_symbols);
                        new_rules.put(new_non_terminal, setProd);
                        rhs_to_non_terminal.put(first_two_symbols, new_non_terminal);
                    }
                    production = new_non_terminal + production.substring(2);
                }
                new_rules.get(non_terminal).add(production);
            }
        }

        // Handle mixed productions
        for (Map.Entry<String, Set<String>> entry : new LinkedHashMap<>(new_rules).entrySet()) {
            String non_terminal = entry.getKey();
            Set<String> productions = new_rules.get(non_terminal);
            // Create a copy of productions to iterate over
            Set<String> temp_productions = new LinkedHashSet<>(productions);
            for (String production : temp_productions) {
                if (production.length() == 2) {
                    boolean hasTerminal = false;
                    for (int i = 0; i < production.length(); i++) {
                        String symbol = String.valueOf(production.charAt(i));
                        if (grammar.terminals.contains(symbol)) {
                            hasTerminal = true;
                            break;
                        }
                    }
                    if (hasTerminal) {
                        List<String> new_production = new ArrayList<>();
                        for (int i = 0; i < production.length(); i++) {
                            String symbol = String.valueOf(production.charAt(i));
                            if (grammar.terminals.contains(symbol)) {
                                String new_non_terminal;
                                if (rhs_to_non_terminal.containsKey(symbol)) {
                                    new_non_terminal = rhs_to_non_terminal.get(symbol);
                                } else {
                                    new_non_terminal = grammar._create_new_non_terminal();
                                    Set<String> setProd = new LinkedHashSet<>();
                                    setProd.add(symbol);
                                    new_rules.put(new_non_terminal, setProd);
                                    rhs_to_non_terminal.put(symbol, new_non_terminal);
                                }
                                new_production.add(new_non_terminal);
                            } else {
                                new_production.add(symbol);
                            }
                        }
                        productions.remove(production);
                        productions.add(String.join("", new_production));
                    }
                }
            }
        }

        // Combine old non-terminals and new ones (keys in new_rules not in old_non_terminals)
        LinkedHashMap<String, List<String>> finalRules = new LinkedHashMap<>();
        List<String> combinedKeys = new ArrayList<>(old_non_terminals);
        for (String key : new_rules.keySet()) {
            if (!old_non_terminals.contains(key)) {
                combinedKeys.add(key);
            }
        }
        // Sort combinedKeys so that non-terminals that start with a letter come before those that start with a digit
        combinedKeys.sort((s1, s2) -> {
            boolean s1Letter = Character.isLetter(s1.charAt(0));
            boolean s2Letter = Character.isLetter(s2.charAt(0));
            if (s1Letter && !s2Letter) {
                return -1;
            } else if (!s1Letter && s2Letter) {
                return 1;
            } else {
                return s1.compareTo(s2);
            }
        });
        for (String nt : combinedKeys) {
            if (new_rules.containsKey(nt)) {
                finalRules.put(nt, new ArrayList<>(new_rules.get(nt)));
            }
        }
        grammar.rules = finalRules;

        System.out.println("Conversion to Chomsky Normal Form:");
        grammar.print_rules();
    }
}
