package av.biezbardis.mentorship.tasks.fourth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CharCounterExecutor {
    private final CharCounter counter;
    private final ReportConstructor reportConstructor;
    private final Map<String, String> cache;

    public CharCounterExecutor(CharCounter counter, ReportConstructor reportConstructor) {
        this.counter = counter;
        this.reportConstructor = reportConstructor;
        this.cache = new ConcurrentHashMap<>();
    }

    public String execute(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }
        if (cache.containsKey(input)) {
            return cache.get(input);
        }
        String report = reportConstructor.construct(input, counter.count(input));
        cache.put(input, report);
        return report;
    }
}
