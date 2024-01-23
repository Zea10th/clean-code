package av.biezbardis.mentorship.tasks.fifth;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReportConstructorImpl implements ReportConstructor {
    private static final String RESULTS_MUST_CONTAIN_AT_LEAST_ONE_VALUE =
            "Provided results must contain at least one Racer-Time value.";
    private static final int QUALIFYING_RACERS = 15;

    @Override
    public String construct(Map<Racer, String> results) {
        if (results.isEmpty()) {
            throw new IllegalArgumentException(RESULTS_MUST_CONTAIN_AT_LEAST_ONE_VALUE);
        }

        return buildReport(results, buildReportPattern(results));
    }

    private static String buildReportPattern(Map<Racer, String> results) {
        int firstColumnWidth = (int) (Math.log10(results.size()) + 1);
        int secondColumnWidth = getColumnWidth(results, Racer::name);
        int thirdColumnWidth = getColumnWidth(results, Racer::car);

        return "%" + firstColumnWidth + "d. %-"
                + secondColumnWidth + "s | %-"
                + thirdColumnWidth + "s | %s%n";
    }

    private String buildReport(Map<Racer, String> results, String reportPattern) {
        AtomicInteger counter = new AtomicInteger(0);

        List<String> reportList = results.entrySet().stream()
                .map(entry -> String.format(reportPattern,
                        counter.incrementAndGet(),
                        entry.getKey().name(),
                        entry.getKey().car(),
                        entry.getValue()))
                .toList();

        if (counter.get() > QUALIFYING_RACERS) {
            reportList = separateQualifiedRacers(reportList);
        }

        return reportList.stream()
                .collect(Collectors.joining());
    }

    private List<String> separateQualifiedRacers(List<String> reportList) {
        List<String> result = new LinkedList<>(reportList);
        String lineSeparator = "-".repeat(reportList.get(0).length()) + "\n";
        result.add(QUALIFYING_RACERS, lineSeparator);
        return result;
    }

    private static int getColumnWidth(Map<Racer, String> results, Function<Racer, String> calculationField) {
        return results.keySet().stream()
                .map(calculationField)
                .mapToInt(String::length)
                .max().orElseThrow();
    }
}
