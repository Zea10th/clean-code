package av.biezbardis.mentorship.tasks.third;

public class DivisionCounter {
    static final String DIVISOR_CANNOT_BE_0_MESSAGE = "Divisor cannot be 0, result is not integer.";

    public int[] count(int dividend, int divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException(DIVISOR_CANNOT_BE_0_MESSAGE);
        }

        int quotient = dividend / divisor;

        int[] divisionElements;

        if (quotient == 0) {
            divisionElements = new int[]{
                    dividend,
                    0,
                    dividend % divisor,
                    quotient,
                    divisor};
            return divisionElements;
        }

        divisionElements = getDivisionElements(dividend, divisor, quotient);
        return divisionElements;
    }

    private int[] getDivisionElements(int dividend, int divisor, int quotient) {
        String quotientString = Integer.toString(quotient);
        StringBuilder dividendString = new StringBuilder(Integer.toString(dividend));

        int elementsAmount = 3 + 2 * quotientString.length();
        int[] divisionElements = new int[elementsAmount];

        for (int charIndex = 1, pairIndex = 0; charIndex < dividendString.length() + 1; charIndex++) {
            int evenIndex = pairIndex * 2;
            int oddIndex = evenIndex + 1;

            int quotientDigit = Character.getNumericValue(quotientString.charAt(pairIndex));
            int oddElement = quotientDigit * divisor;
            int evenElement = Integer.parseInt(dividendString.substring(0, charIndex));

            if (evenElement >= oddElement) {
                dividendString.replace(0, charIndex, String.valueOf(evenElement - oddElement));

                divisionElements[evenIndex] = evenElement;
                divisionElements[oddIndex] = oddElement;
                pairIndex++;
                charIndex = 1;
            }
        }

        divisionElements[0] = dividend;
        divisionElements[elementsAmount - 3] = dividend % divisor;
        divisionElements[elementsAmount - 2] = quotient;
        divisionElements[elementsAmount - 1] = divisor;

        return divisionElements;
    }
}