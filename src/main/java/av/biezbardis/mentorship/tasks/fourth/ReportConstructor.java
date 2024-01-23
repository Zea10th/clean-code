package av.biezbardis.mentorship.tasks.fourth;

import java.util.Map;
import java.util.stream.Collectors;

public class ReportConstructor {
    public String construct(String input, Map<Character, Integer> count) {
        return input
                + System.lineSeparator()
                + count.entrySet().stream()
                .map(entry -> String.format("'%c' - %d%n", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining());
    }
}