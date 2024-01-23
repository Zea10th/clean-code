package av.biezbardis.mentorship.tasks.fifth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ReportConstructorTest {
    private static final InputStream ABBREVIATIONS_STREAM = ReportConstructorTest.class
            .getClassLoader().getResourceAsStream("fifth/abbreviations.txt");
    private static final InputStream FIVE_ABBREVIATIONS_STREAM = new ByteArrayInputStream(
            new BufferedReader(new InputStreamReader(ABBREVIATIONS_STREAM))
                    .lines()
                    .limit(5)
                    .collect(Collectors.joining()).getBytes()
    );
    private static final InputStream FIFTEEN_ABBREVIATIONS_STREAM = new ByteArrayInputStream(
            new BufferedReader(new InputStreamReader(ABBREVIATIONS_STREAM))
                    .lines()
                    .limit(15)
                    .collect(Collectors.joining()).getBytes()
    );
    private static final InputStream START_LOGS_STREAM = ReportConstructorTest.class
            .getClassLoader().getResourceAsStream("fifth/start.log");
    private static final InputStream END_LOGS_STREAM = ReportConstructorTest.class
            .getClassLoader().getResourceAsStream("fifth/end.log");
    public static final String RESULTS_MUST_CONTAIN_AT_LEAST_ONE_VALUE =
            "Provided results must contain at least one Racer-Time value.";

    @Mock
    private RaceServiceImpl raceService;
    @InjectMocks
    private ReportConstructorImpl constructor;

    @BeforeEach
    void setUp() {
        Map<Racer, String> fiveResults = new LinkedHashMap<>();
        fiveResults.put(new Racer("Sebastian Vettel", "FERRARI"), "1:04.415");
        fiveResults.put(new Racer("Daniel Ricciardo", "RED BULL RACING TAG HEUER"), "1:12.013");
        fiveResults.put(new Racer("Valtteri Bottas", "MERCEDES"), "1:12.434");
        fiveResults.put(new Racer("Lewis Hamilton", "MERCEDES"), "1:12.460");
        fiveResults.put(new Racer("Stoffel Vandoorne", "MCLAREN RENAULT"), "1:12.463");

        Map<Racer, String> fifteenResults = new LinkedHashMap<>(fiveResults);
        fifteenResults.put(new Racer("Kimi Raikkonen", "FERRARI"), "1:12.639");
        fifteenResults.put(new Racer("Fernando Alonso", "MCLAREN RENAULT"), "1:12.657");
        fifteenResults.put(new Racer("Sergey Sirotkin", "WILLIAMS MERCEDES"), "1:12.706");
        fifteenResults.put(new Racer("Charles Leclerc", "SAUBER FERRARI"), "1:12.829");
        fifteenResults.put(new Racer("Sergio Perez", "FORCE INDIA MERCEDES"), "1:12.848");
        fifteenResults.put(new Racer("Romain Grosjean", "HAAS FERRARI"), "1:12.930");
        fifteenResults.put(new Racer("Pierre Gasly", "SCUDERIA TORO ROSSO HONDA"), "1:12.941");
        fifteenResults.put(new Racer("Carlos Sainz", "RENAULT"), "1:12.950");
        fifteenResults.put(new Racer("Esteban Ocon", "FORCE INDIA MERCEDES"), "1:13.028");
        fifteenResults.put(new Racer("Nico Hulkenberg", "RENAULT"), "1:13.065");

        Map<Racer, String> fullResults = new LinkedHashMap<>(fifteenResults);
        fullResults.put(new Racer("Brendon Hartley", "SCUDERIA TORO ROSSO HONDA"), "1:13.179");
        fullResults.put(new Racer("Marcus Ericsson", "SAUBER FERRARI"), "1:13.265");
        fullResults.put(new Racer("Lance Stroll", "WILLIAMS MERCEDES"), "1:13.323");
        fullResults.put(new Racer("Kevin Magnussen", "HAAS FERRARI"), "1:13.393");

        lenient().when(raceService.process(FIVE_ABBREVIATIONS_STREAM, START_LOGS_STREAM, END_LOGS_STREAM))
                .thenReturn(fiveResults);
        lenient().when(raceService.process(FIFTEEN_ABBREVIATIONS_STREAM, START_LOGS_STREAM, END_LOGS_STREAM))
                .thenReturn(fifteenResults);
        lenient().when(raceService.process(ABBREVIATIONS_STREAM, START_LOGS_STREAM, END_LOGS_STREAM))
                .thenReturn(fullResults);

        lenient().when(raceService.process(null, null, null))
                .thenReturn(Collections.emptyMap());
    }

    @Test
    void shouldReturnCorrectReportWithPassedRacersWhenProvidedCorrectResultsMap() {
        final String expected = """
                 1. Sebastian Vettel  | FERRARI                   | 1:04.415
                 2. Daniel Ricciardo  | RED BULL RACING TAG HEUER | 1:12.013
                 3. Valtteri Bottas   | MERCEDES                  | 1:12.434
                 4. Lewis Hamilton    | MERCEDES                  | 1:12.460
                 5. Stoffel Vandoorne | MCLAREN RENAULT           | 1:12.463
                 6. Kimi Raikkonen    | FERRARI                   | 1:12.639
                 7. Fernando Alonso   | MCLAREN RENAULT           | 1:12.657
                 8. Sergey Sirotkin   | WILLIAMS MERCEDES         | 1:12.706
                 9. Charles Leclerc   | SAUBER FERRARI            | 1:12.829
                10. Sergio Perez      | FORCE INDIA MERCEDES      | 1:12.848
                11. Romain Grosjean   | HAAS FERRARI              | 1:12.930
                12. Pierre Gasly      | SCUDERIA TORO ROSSO HONDA | 1:12.941
                13. Carlos Sainz      | RENAULT                   | 1:12.950
                14. Esteban Ocon      | FORCE INDIA MERCEDES      | 1:13.028
                15. Nico Hulkenberg   | RENAULT                   | 1:13.065
                -------------------------------------------------------------
                16. Brendon Hartley   | SCUDERIA TORO ROSSO HONDA | 1:13.179
                17. Marcus Ericsson   | SAUBER FERRARI            | 1:13.265
                18. Lance Stroll      | WILLIAMS MERCEDES         | 1:13.323
                19. Kevin Magnussen   | HAAS FERRARI              | 1:13.393
                """;
        String actual = constructor.construct(raceService
                .process(ABBREVIATIONS_STREAM, START_LOGS_STREAM, END_LOGS_STREAM));
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectReportWith5PassedRacersWOSpaceAtTheBeginningWhenProvidedResultsMapOf5Racers() {
        final String expected = """
                1. Sebastian Vettel  | FERRARI                   | 1:04.415
                2. Daniel Ricciardo  | RED BULL RACING TAG HEUER | 1:12.013
                3. Valtteri Bottas   | MERCEDES                  | 1:12.434
                4. Lewis Hamilton    | MERCEDES                  | 1:12.460
                5. Stoffel Vandoorne | MCLAREN RENAULT           | 1:12.463
                """;
        String actual = constructor.construct(raceService
                .process(FIVE_ABBREVIATIONS_STREAM, START_LOGS_STREAM, END_LOGS_STREAM));
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectReportWith15PassedRacersWhenProvidedResultsMapOf15Racers() {
        final String expected = """
                 1. Sebastian Vettel  | FERRARI                   | 1:04.415
                 2. Daniel Ricciardo  | RED BULL RACING TAG HEUER | 1:12.013
                 3. Valtteri Bottas   | MERCEDES                  | 1:12.434
                 4. Lewis Hamilton    | MERCEDES                  | 1:12.460
                 5. Stoffel Vandoorne | MCLAREN RENAULT           | 1:12.463
                 6. Kimi Raikkonen    | FERRARI                   | 1:12.639
                 7. Fernando Alonso   | MCLAREN RENAULT           | 1:12.657
                 8. Sergey Sirotkin   | WILLIAMS MERCEDES         | 1:12.706
                 9. Charles Leclerc   | SAUBER FERRARI            | 1:12.829
                10. Sergio Perez      | FORCE INDIA MERCEDES      | 1:12.848
                11. Romain Grosjean   | HAAS FERRARI              | 1:12.930
                12. Pierre Gasly      | SCUDERIA TORO ROSSO HONDA | 1:12.941
                13. Carlos Sainz      | RENAULT                   | 1:12.950
                14. Esteban Ocon      | FORCE INDIA MERCEDES      | 1:13.028
                15. Nico Hulkenberg   | RENAULT                   | 1:13.065
                """;
        String actual = constructor.construct(raceService
                .process(FIFTEEN_ABBREVIATIONS_STREAM, START_LOGS_STREAM, END_LOGS_STREAM));
        assertEquals(expected, actual);
    }


    @Test
    void shouldReturnIllegalArgumentExceptionWhenProvidedEmptyInput() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> constructor.construct(raceService.process(null, null, null)));
        assertEquals(RESULTS_MUST_CONTAIN_AT_LEAST_ONE_VALUE, thrown.getMessage());
    }
}
