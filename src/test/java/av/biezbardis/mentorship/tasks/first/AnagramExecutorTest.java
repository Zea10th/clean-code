package av.biezbardis.mentorship.tasks.first;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;


class AnagramExecutorTest {
    private AnagramExecutor executor;

    @BeforeEach
    void init() {
        executor = new AnagramExecutor();
    }

    private static Stream<Arguments> nullAndEmptyArguments() {
        return Stream.of(
                arguments(named("null", null)),
                arguments(named("empty string", ""))
        );
    }

    private static Stream<Arguments> blankArguments() {
        return Stream.of(
                arguments(named("One space", " ")),
                arguments(named("Two spaces", "  ")),
                arguments(named("Three spaces", "   ")),
                arguments(named("Tab", "\t")),
                arguments(named("New line", "\n"))
        );
    }

    @ParameterizedTest(name = "[{index}] input is {0}")
    @MethodSource("nullAndEmptyArguments")
    void shouldThrowIllegalStateExceptionWhenSentNullOrEmptyString(String input) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> executor.execute(input));
        assertTrue(thrown.getMessage().contains("An input text can't be null or empty"));
    }

    @ParameterizedTest(name = "[{index}] input is {0}")
    @MethodSource("blankArguments")
    void shouldReturnEmptyStringWhenSentAnyTypesOfBlanks(String input) {
        String expected = "";
        String actual = executor.execute(input);
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnOneLetterWhenSentLetter() {
        String expected = "x";
        String actual = executor.execute("x");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnSameLettersWhenSentSameLetters() {
        String expected = "vvv";
        String actual = executor.execute("vvv");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnReversedWordWhenSentWord() {
        String expected = "dcba";
        String actual = executor.execute("abcd");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnReversedUppercaseWordWhenSentUppercaseWord() {
        String expected = "DCBA";
        String actual = executor.execute("ABCD");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnReversedLowerAndUppercaseWordWhenSentLowerAndUppercase() {
        String expected = "dCbA";
        String actual = executor.execute("AbCd");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnSameDigitWhenSentDigit() {
        String expected = "1234";
        String actual = executor.execute("1234");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnSameSpecialSymbolsWhenSentSpecialSymbols() {
        String expected = "!@#$";
        String actual = executor.execute("!@#$");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnReversedWordWithDigitsOnSamePlaceWhenSentWordWithDigits() {
        String expected = "d1cb3a4";
        String actual = executor.execute("a1bc3d4");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnReversedSentenceOnlyOfLettersWhenSentSentenceOnlyOfLetters() {
        String expected = "olleH dlroW";
        String actual = executor.execute("Hello World");
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnReversedMixedSentenceWhenSentMixedSentence() {
        String expected = "d1cba hgf!e";
        String actual = executor.execute("a1bcd efg!h");
        assertEquals(expected, actual);
    }
}