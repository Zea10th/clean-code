package av.biezbardis.mentorship.tasks.first;

public class AnagramExecutor {
    public static final String SPACE_DELIMITER = " ";

    public String execute(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("An input text can't be null or empty.");
        }

        String[] words = input.split(SPACE_DELIMITER);

        StringBuilder resultBuilder = new StringBuilder();

        for (String word : words) {
            resultBuilder.append(reverseWord(word)).append(SPACE_DELIMITER);
        }

        return resultBuilder.toString().trim();
    }

    private String reverseWord(String word) {
        char[] reversedSymbols = new char[word.length()];

        for (int leftCounter = 0, rightCounter = word.length() - 1; leftCounter <= rightCounter; ) {
            char leftSymbol = word.charAt(leftCounter);
            char rightSymbol = word.charAt(rightCounter);

            if (!Character.isLetter(leftSymbol)) {
                reversedSymbols[leftCounter] = leftSymbol;
                leftCounter++;
            } else if (!Character.isLetter(rightSymbol)) {
                reversedSymbols[rightCounter] = rightSymbol;
                rightCounter--;
            } else {
                reversedSymbols[leftCounter] = rightSymbol;
                reversedSymbols[rightCounter] = leftSymbol;
                leftCounter++;
                rightCounter--;
            }
        }

        return String.copyValueOf(reversedSymbols);
    }
}
