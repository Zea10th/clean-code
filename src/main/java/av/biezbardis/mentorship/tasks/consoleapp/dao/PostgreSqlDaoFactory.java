package av.biezbardis.mentorship.tasks.consoleapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A class-configuration that has credentials for connecting a PostgreSQL Database
 * and a method that provides connection to that Database and scheme.
 */
public class PostgreSqlDaoFactory {
    private static PostgreSqlDaoFactory instance;
    private static final String URL = "jdbc:postgresql://localhost:5432/school_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private PostgreSqlDaoFactory() {
    }

    public static PostgreSqlDaoFactory getInstance() {
        if (instance == null) {
            instance = new PostgreSqlDaoFactory();
        }
        return instance;
    }

    /**
     * Attempts to establish a connection to the PostgreSql Database
     *
     * @return a connection to the URL with provided credentials
     * @throws SQLException – if a database access error occurs or the url is null
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
