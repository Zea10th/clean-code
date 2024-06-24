package av.biezbardis.mentorship.tasks.plainconsoleapp.utility;

import av.biezbardis.mentorship.tasks.plainconsoleapp.dao.ConnectionUtil;
import av.biezbardis.mentorship.tasks.plainconsoleapp.exception.DataAccessException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseManager {
    private static final InputStream SCHEME_SQL_QUERY_STREAM = DatabaseManager.class
            .getClassLoader().getResourceAsStream("plainconsoleapp/scheme.sql");
    private static final InputStream SCRIPT_SQL_QUERY_STREAM = DatabaseManager.class
            .getClassLoader().getResourceAsStream("plainconsoleapp/script.sql");
    private static final String DROP_TABLES_IF_EXISTS =
            "DROP TABLE IF EXISTS students, groups, courses, course_students;";

    public void execute() {
        try (Connection connection = ConnectionUtil.getInstance().getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(DROP_TABLES_IF_EXISTS);

            statement.executeUpdate(executeSqlQuery(SCHEME_SQL_QUERY_STREAM));
            statement.executeUpdate(executeSqlQuery(SCRIPT_SQL_QUERY_STREAM));
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create scheme or fill up tables", e);
        }
    }

    private String executeSqlQuery(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
