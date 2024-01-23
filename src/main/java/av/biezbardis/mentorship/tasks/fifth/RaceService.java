package av.biezbardis.mentorship.tasks.fifth;

import java.io.InputStream;
import java.util.Map;

public interface RaceService {
    Map<Racer, String> process(
            InputStream abbreviationsStream, InputStream startLogsStream, InputStream endLogsStream);
}
