package av.biezbardis.mentorship.tasks.fifth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RaceServiceTest {
    private static final InputStream ABBREVIATIONS_STREAM = RaceServiceTest.class
            .getClassLoader().getResourceAsStream("fifth/abbreviations.txt");
    private static final InputStream START_LOGS_STREAM = RaceServiceTest.class
            .getClassLoader().getResourceAsStream("fifth/start.log");
    private static final InputStream END_LOGS_STREAM = RaceServiceTest.class
            .getClassLoader().getResourceAsStream("fifth/end.log");
    private static final String INPUT_CANT_BE_NULL = "Input can't be null.";

    @InjectMocks
    private RaceServiceImpl raceService;

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSentNulls() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> raceService.process(null, null, null));
        assertEquals(INPUT_CANT_BE_NULL, thrown.getMessage());
    }

    @Test
    void shouldReturnEmptyMapWhenSentEmptyInputs() {
        InputStream empty = new ByteArrayInputStream(new byte[0]);
        Map<Racer, String> actual = raceService.process(empty, empty, empty);
        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldReturnRacersResultsWhenSentCorrectData() {
        final Map<Racer, String> expected = Map.of(
                new Racer("Daniel Ricciardo", "RED BULL RACING TAG HEUER"), "1:12.013",
                new Racer("Sebastian Vettel", "FERRARI"), "1:04.415",
                new Racer("Lewis Hamilton", "MERCEDES"), "1:12.460"
        );

        Map<Racer, String> actual = raceService.process(ABBREVIATIONS_STREAM, START_LOGS_STREAM, END_LOGS_STREAM);

        assertTrue(actual.entrySet().containsAll(expected.entrySet()));
    }

    @Test
    void shouldReturn19RacersResultsWhenSentCorrectData() {
        final int expected = 19;
        Map<Racer, String> processed = raceService.process(ABBREVIATIONS_STREAM, START_LOGS_STREAM, END_LOGS_STREAM);
        int actual = processed.size();
        assertEquals(expected, actual);
    }
}