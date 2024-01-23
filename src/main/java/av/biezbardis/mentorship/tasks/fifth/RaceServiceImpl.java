package av.biezbardis.mentorship.tasks.fifth;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class RaceServiceImpl implements RaceService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss.SSS");
    private static final String INPUT_CANT_BE_NULL_MSG = "Input can't be null.";
    @Override
    public Map<Racer, String> process(
            InputStream abbreviationsStream, InputStream startLogsStream, InputStream endLogsStream) {

        if (abbreviationsStream == null || startLogsStream == null || endLogsStream == null) {
            throw new IllegalArgumentException(INPUT_CANT_BE_NULL_MSG);
        }

        Map<String, LocalDateTime> startLogs = parseLogTimes(readStream(startLogsStream));
        Map<String, LocalDateTime> endLogs = parseLogTimes(readStream(endLogsStream));
        Map<String, String> lapTimes = calculateLapTimes(startLogs, endLogs);
        Map<String, Racer> racers = parseRacers(abbreviationsStream);

        return getQualificationResults(lapTimes, racers);
    }

    private static Map<Racer, String> getQualificationResults(Map<String, String> lapTimes, Map<String, Racer> racers) {
        return lapTimes.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        entry -> racers.get(entry.getKey()),
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Racer> parseRacers(InputStream abbreviationsStream) {
        return readStream(abbreviationsStream).stream()
                .map(row -> row.split("_"))
                .collect(Collectors.toMap(
                        racerInfo -> racerInfo[0],
                        racerInfo -> new Racer(racerInfo[1], racerInfo[2])));
    }

    private Map<String, String> calculateLapTimes(Map<String, LocalDateTime> startLogs, Map<String, LocalDateTime> endLogs) {
        return startLogs.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> countLapTime(
                                entry.getValue(),
                                endLogs.get(entry.getKey()))
                ));
    }

    private List<String> readStream(InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream))
                .lines()
                .toList();
    }

    private String countLapTime(LocalDateTime start, LocalDateTime end) {
        long lapTime = ChronoUnit.MILLIS.between(start, end);

        Duration duration = Duration.ofMillis(lapTime);
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();
        long millis = duration.minusMinutes(minutes).minusSeconds(seconds).toMillis();

        return String.format("%d:%02d.%03d", minutes, seconds, millis);
    }

    private Map<String, LocalDateTime> parseLogTimes(List<String> logList) {
        return logList.stream()
                .collect(Collectors.toMap(
                        log -> log.substring(0, 3),
                        log -> LocalDateTime.parse(log.substring(3), DATE_TIME_FORMATTER)
                ));
    }
}
