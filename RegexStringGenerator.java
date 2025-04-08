import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegexStringGenerator {
    private Random random = new Random();

    public String generateString(String re) {
        StringBuilder result = new StringBuilder();
        char[] reArr = re.toCharArray();

        for (int i = 0; i < reArr.length; i++) {
            char current = reArr[i];

            if (current == '?' && isCharOrDigit(reArr[i - 1]) && reArr[i - 1] != ')') {
                if (random.nextInt(2) == 1) result.append(reArr[i - 1]);
            } else if (current == '*' && isCharOrDigit(reArr[i - 1]) && reArr[i - 1] != ')') {
                result.append(String.valueOf(reArr[i - 1]).repeat(random.nextInt(6)));
            } else if (current == '+' && isCharOrDigit(reArr[i - 1]) && reArr[i - 1] != ')') {
                result.append(String.valueOf(reArr[i - 1]).repeat(1 + random.nextInt(5)));
            } else if (current == '^' && isCharOrDigit(reArr[i - 1]) && reArr[i - 1] != ')') {
                int repetitions = Character.getNumericValue(reArr[i + 1]);
                result.append(String.valueOf(reArr[i - 1]).repeat(repetitions));
                i++;
            } else if (current == '(' && i + 4 < reArr.length && reArr[i + 2] == '|' && reArr[i + 4] == ')') {
                char selected = random.nextBoolean() ? reArr[i + 1] : reArr[i + 3];
                i = handleRepetition(reArr, i + 5, selected, result);
            } else if (current == '(' && i + 6 < reArr.length && reArr[i + 2] == '|' && reArr[i + 4] == '|' && reArr[i + 6] == ')') {
                int choice = random.nextInt(3);
                char selected = switch (choice) {
                    case 0 -> reArr[i + 1];
                    case 1 -> reArr[i + 3];
                    default -> reArr[i + 5];
                };
                i = handleRepetition(reArr, i + 7, selected, result);
            } else if (isCharOrDigit(current) &&
                    (i == reArr.length - 1 || "^?*+".indexOf(reArr[i + 1]) == -1)) {
                result.append(current);
            }
        }

        return result.toString();
    }

    public List<String> generateMultipleStrings(String re, int count) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            results.add(generateString(re));
        }
        return results;
    }

    public List<String> explainProcessing(String re) {
        List<String> steps = new ArrayList<>();
        steps.add("\nREGULAR EXPRESSION: " + "\"" + re + "\"");

        char[] reArr = re.toCharArray();
        int currentPos = 0;

        while (currentPos < reArr.length) {
            char current = reArr[currentPos];

            if (current == '?') {
                steps.add(" " + steps.size() + ": '?' after '" + reArr[currentPos - 1] + "' — 0 or 1 occurrence");
                currentPos++;
            } else if (current == '*') {
                steps.add(" " + steps.size() + ": '*' after '" + reArr[currentPos - 1] + "' — 0 to 5 occurrences");
                currentPos++;
            } else if (current == '+') {
                steps.add(" " + steps.size() + ": '+' after '" + reArr[currentPos - 1] + "' — 1 to 5 occurrences");
                currentPos++;
            } else if (current == '^') {
                steps.add(" " + steps.size() + ": '^" + reArr[currentPos + 1] + "' — repeat '" + reArr[currentPos - 1] + "' exactly " + reArr[currentPos + 1] + " times");
                currentPos += 2;
            } else if (current == '(') {
                int close = currentPos;
                while (close < reArr.length && reArr[close] != ')') close++;
                String group = new String(reArr, currentPos + 1, close - currentPos - 1);
                steps.add(" " + steps.size() + ": Alternation group (" + group + ") — select one randomly");

                if (close + 1 < reArr.length) {
                    char next = reArr[close + 1];
                    if (next == '^') {
                        steps.add(" " + steps.size() + ": Repeat selected option " + reArr[close + 2] + " times");
                    } else if ("?*+".indexOf(next) != -1) {
                        steps.add(" " + steps.size() + ": Repeat selected option using operator '" + next + "'");
                    }
                }

                currentPos = close + 1;
            } else if (isCharOrDigit(current)) {
                steps.add(" " + steps.size() + ": Found character '" + current + "'");
                currentPos++;
            } else {
                currentPos++;
            }
        }

        steps.add(" " + steps.size() + ": Generation complete");
        return steps;
    }

    private boolean isCharOrDigit(char c) {
        return Character.isLetterOrDigit(c);
    }

    private int handleRepetition(char[] reArr, int index, char selected, StringBuilder result) {
        if (index < reArr.length) {
            char next = reArr[index];
            if (next == '^') {
                int repetitions = Character.getNumericValue(reArr[index + 1]);
                result.append(String.valueOf(selected).repeat(repetitions));
                return index + 1;
            } else if ("?*+".indexOf(next) != -1) {
                int repetitions = getRepetitionCount(next);
                result.append(String.valueOf(selected).repeat(repetitions));
                return index;
            }
        }
        result.append(selected);
        return index - 1;
    }

    private int getRepetitionCount(char operator) {
        return switch (operator) {
            case '?' -> random.nextInt(2);
            case '*' -> random.nextInt(6);
            case '+' -> 1 + random.nextInt(5);
            default -> 1;
        };
    }
}
