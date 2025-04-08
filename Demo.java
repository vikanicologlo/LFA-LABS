import java.util.List;

public class Demo {
    private static final RegexStringGenerator generator = new RegexStringGenerator();

    public static void main(String[] args) {
        String[] regexList = {
                "(a|b)(c|d)E+G?",
                "P(Q|R|S)T(U|V|W|X)*Z+",
                "1(0|1)*2(3|4)^5 36"
        };

        for (int i = 0; i < regexList.length; i++) {
            String regex = regexList[i];
            explainRegex(regex);
            generateSamples(regex, 5);
        }
    }

    private static void explainRegex(String regex) {
        List<String> steps = generator.explainProcessing(regex);
        for (String step : steps) {
            System.out.println("   " + step);
        }
    }

    private static void generateSamples(String regex, int count) {
        System.out.println("\nGenerated Strings:");
        List<String> generated = generator.generateMultipleStrings(regex, count);
        for (int i = 0; i < generated.size(); i++) {
            System.out.println("-" + generated.get(i));
        }
    }
}
