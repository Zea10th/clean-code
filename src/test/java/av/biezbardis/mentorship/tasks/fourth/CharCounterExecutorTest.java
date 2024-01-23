package av.biezbardis.mentorship.tasks.fourth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CharCounterExecutorTest {
    private static final String HELLO_WORLD = "Hello World!";
    public static final String SPACES = "    ";

    @Mock
    private CharCounter counter;
    @Mock
    private ReportConstructor constructor;
    @InjectMocks
    private CharCounterExecutor executor;

    private static Stream<Arguments> nullAndEmptyArguments() {
        return Stream.of(
                arguments(named("null", null)),
                arguments(named("empty string", ""))
        );
    }

    @ParameterizedTest(name = "[{index}] input is {0}")
    @MethodSource("nullAndEmptyArguments")
    void shouldThrowIllegalStateExceptionWhenSentNullOrEmptyString(String input) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> executor.execute(input));
        assertTrue(thrown.getMessage().contains("Input cannot be empty."));
    }

    @Test
    void shouldReturnCorrectReportWhenProvidedHelloWorld() {
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
        String actual = executor.execute(HELLO_WORLD);
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnSpacesWhenProvidedSpaces() {
        String expected = "    \n' ' - 4\n";
        String actual = executor.execute(SPACES);
        assertEquals(expected, actual);
    }

    @Test
    void shouldPassWhenProvidedFewInputsAfterFewSameInputs() {
        Instant firstStart = Instant.now();
        executor.execute(HELLO_WORLD);
        executor.execute(SPACES);
        Instant firstStop = Instant.now();

        Instant secondStart = Instant.now();
        executor.execute(HELLO_WORLD);
        executor.execute(SPACES);
        Instant secondStop = Instant.now();

        long difference = Duration.between(firstStart, firstStop).toMillis()
                - Duration.between(secondStart, secondStop).toMillis();

        assertTrue(difference > 0,
                String.format("Difference is %d", difference));
    }

    @BeforeEach
    void setUp() {
        when(counter.count(HELLO_WORLD))
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

        lenient().when(constructor.construct(HELLO_WORLD, counter.count(HELLO_WORLD)))
                .thenReturn("""
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
                        """
                );

        lenient().when(counter.count(SPACES))
                .thenReturn(new LinkedHashMap<>() {{
                    put(' ', 4);
                }});

        lenient().when(constructor.construct(SPACES, counter.count(SPACES)))
                .thenReturn("    \n' ' - 4\n");
    }
}