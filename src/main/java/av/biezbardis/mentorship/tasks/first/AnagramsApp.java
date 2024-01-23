package av.biezbardis.mentorship.tasks.first;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AnagramsApp {
    public static void main(String[] args) throws IOException {
        AnagramExecutor application = new AnagramExecutor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        String anagram = application.execute(input);
        System.out.printf("Anagram for \"%s\" is: \"%s\".\n",
                input,
                anagram);
    }
}
