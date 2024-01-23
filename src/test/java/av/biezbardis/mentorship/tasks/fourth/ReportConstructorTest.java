package av.biezbardis.mentorship.tasks.fourth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportConstructorTest {
    @Mock
    private CharCounter counter;
    @InjectMocks
    private ReportConstructor constructor;

    @Test
    void shouldReturnSpacesWhenProvidedSpaces() {
        String input = "    ";

        when(counter.count(input))
                .thenReturn(new LinkedHashMap<>() {{
                    put(' ', 4);
                }});

        String expected ="    \n' ' - 4\n";
        String actual = constructor.construct(input, counter.count(input));
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectReportWhenProvidedHelloWorld() {
        String input = "Hello World!";

        when(counter.count(input))
                .thenReturn(new LinkedHashMap<>() {{
                    put('h', 1);
                    put('e', 1);
                    put('l', 3);
                    put('o', 2);
                    put(' ', 1);
                    put('w', 1);
                    put('r', 1);
                    put('d', 1);
                    put('!', 1);
                }});

        String expected = """
                Hello World!
                'h' - 1
                'e' - 1
                'l' - 3
                'o' - 2
                ' ' - 1
                'w' - 1
                'r' - 1
                'd' - 1
                '!' - 1
                """;
        String actual = constructor.construct(input, counter.count(input));
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectReportWhenProvidedRedColour() {
        String input = "Red Colour?";

        when(counter.count(input))
                .thenReturn(new LinkedHashMap<>() {{
                    put('r', 2);
                    put('e', 1);
                    put('d', 1);
                    put(' ', 1);
                    put('c', 1);
                    put('o', 2);
                    put('l', 1);
                    put('u', 1);
                    put('?', 1);
                }});

        String expected = """
                Red Colour?
                'r' - 2
                'e' - 1
                'd' - 1
                ' ' - 1
                'c' - 1
                'o' - 2
                'l' - 1
                'u' - 1
                '?' - 1
                """;
        String actual = constructor.construct(input, counter.count(input));
        assertEquals(expected, actual);
    }
}