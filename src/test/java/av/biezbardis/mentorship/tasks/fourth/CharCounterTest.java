package av.biezbardis.mentorship.tasks.fourth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CharCounterTest {
    @InjectMocks
    private CharCounter counter;
    private Map<Character, Integer> expected;

    @Test
    void shouldReturnSpacesWhenProvidedSpaces() {
        expected = Map.of(' ', 4);
        Map<Character, Integer> actual = counter.count("    ");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnAmountByCharWhenProvidedHelloWorld() {
        expected = Map.of(
                'h', 1,
                'e', 1,
                'l', 3,
                'o', 2,
                ' ', 1,
                'w', 1,
                'r', 1,
                'd', 1,
                '!', 1
        );
        Map<Character, Integer> actual = counter.count("Hello world!");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnAmountByCharWhenProvidedRedColour() {
        expected = Map.of(
                'r', 2,
                'e', 1,
                'd', 1,
                ' ', 1,
                'c', 1,
                'o', 2,
                'l', 1,
                'u', 1,
                '?', 1
        );
        Map<Character, Integer> actual = counter.count("Red colour?");
        assertEquals(expected, actual);
    }
}