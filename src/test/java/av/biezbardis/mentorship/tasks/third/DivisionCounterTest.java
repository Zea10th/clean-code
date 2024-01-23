package av.biezbardis.mentorship.tasks.third;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DivisionCounterTest {
    private DivisionCounter divisionCounter;

    @BeforeEach
    void init() {
        divisionCounter = new DivisionCounter();
    }

    @Test
    void shouldReturnIllegalArgumentExceptionInCaseOfDivisorIsZero() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> divisionCounter.count(1, 0));
        assertTrue(thrown.getMessage().contains("Divisor cannot be 0, result is not integer."));
    }

    @Test
    void shouldReturnCorrectListOfElementsWhenProvideValidIntsAsDividendAndDivisor() {
        final int[] expected = {
                78945,
                4,
                38,
                36,
                29,
                28,
                14,
                12,
                25,
                24,
                1,
                19736,
                4
        };
        int[] actual = divisionCounter.count(78945, 4);
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldReturnCorrectListOfElementsWhenProvideSimilarDividendAndDivisor() {
        final int[] expected = {
                500,
                500,
                0,
                4,
                125
        };
        int[] actual = divisionCounter.count(500, 125);
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfFourZeroAndDivisorWhenProvideZeroDividendAndDivisor() {
        final int[] expected = {
                0,
                0,
                0,
                0,
                125
        };
        int[] actual = divisionCounter.count(0, 125);
        assertArrayEquals(expected, actual);
    }
}
