package av.biezbardis.mentorship.tasks.third;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DivisionOutputConstructorTest {
    @Mock
    private DivisionCounter counter;
    @InjectMocks
    private DivisionOutputConstructor outputConstructor = new DivisionOutputConstructor();

    @Test
    void shouldReturnLongDivisionTextWhenProvidedValidDividendAndDivisor() {
        when(counter.count(78945, 4))
                .thenReturn(new int[]{78945, 4, 38, 36, 29, 28, 14, 12, 25, 24, 1, 19736, 4});

        final String expected =
                "_78945|4\n" +
                        " 4    |-----\n" +
                        " -    |19736\n" +
                        "_38\n" +
                        " 36\n" +
                        " --\n" +
                        " _29\n" +
                        "  28\n" +
                        "  --\n" +
                        "  _14\n" +
                        "   12\n" +
                        "   --\n" +
                        "   _25\n" +
                        "    24\n" +
                        "    --\n" +
                        "     1";
        String actual = outputConstructor.construct(counter.count(78945, 4));
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnZeroWhenProvidedDividendLesserThenDivisor() {
        when(counter.count(5, 100))
                .thenReturn(new int[]{5, 0, 5, 0, 100});

        final String expected =
                "_5|100\n" +
                        " 0|---\n" +
                        " -|0\n" +
                        " 5";
        String actual = outputConstructor.construct(counter.count(5, 100));
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnFourAndZeroRemainderWhenProvidedDividendTwentyAndDivisorFive() {
        when(counter.count(20, 5))
                .thenReturn(new int[]{20, 20, 0, 4, 5});

        final String expected =
                "_20|5\n" +
                        " 20|-\n" +
                        " --|4\n" +
                        "  0";
        String actual = outputConstructor.construct(counter.count(20, 5));
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnTwoAndSixAsRemainderWhenProvidedDividendTwentyAndDivisorSeven() {
        when(counter.count(20, 7))
                .thenReturn(new int[]{20, 14, 6, 2, 7});

        final String expected =
                "_20|7\n" +
                        " 14|-\n" +
                        " --|2\n" +
                        "  6";
        String actual = outputConstructor.construct(counter.count(20, 7));
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnOneWhenProvidedSameDividendAndDivisor() {
        when(counter.count(20, 20))
                .thenReturn(new int[]{20, 20, 0, 1, 20});

        final String expected =
                "_20|20\n" +
                        " 20|--\n" +
                        " --|1\n" +
                        "  0";
        String actual = outputConstructor.construct(counter.count(20, 20));
        assertEquals(expected, actual);
    }
}