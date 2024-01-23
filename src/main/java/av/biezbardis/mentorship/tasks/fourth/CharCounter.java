package av.biezbardis.mentorship.tasks.fourth;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CharCounter {
    public Map<Character, Integer> count(String input) {
        return input.chars()
                .mapToObj(symbol -> Character.toLowerCase((char) symbol))
                .collect(Collectors.toMap(
                        Function.identity(),
                        symbol -> 1,
                        Integer::sum,
                        LinkedHashMap::new
                ));
    }
}
