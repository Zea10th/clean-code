package av.biezbardis.mentorship.tasks.plainconsoleapp.dao;

import av.biezbardis.mentorship.tasks.plainconsoleapp.exception.DataBaseCredentialsException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A class that has everything for connecting a Database,
 * a method to get credentials from application.properties file
 * and a method that provides connection to that Database and scheme.
 */
public final class ConnectionUtil {
    private static ConnectionUtil instance;
    private static final String PROPERTIES_FILE = "plainconsoleapp/application.properties";
    private String url;
    private String username;
    private String password;

    private ConnectionUtil() {
        readProperties();
    }

    public static synchronized ConnectionUtil getInstance() {
        if (instance == null) {
            instance = new ConnectionUtil();
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
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Designed to retrieve properties necessary for connecting to a database
     */
    private void readProperties() {
        try (InputStream inputStream = ConnectionUtil.class
                .getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            this.url = properties.getProperty("db.url");
            this.username = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");
        } catch (IOException e) {
            throw new DataBaseCredentialsException("Failed to get properties.", e);
        }
    }
}
