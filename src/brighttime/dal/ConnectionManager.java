package brighttime.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

/**
 * This class manages the database connection.
 *
 * @author annem
 */
public class ConnectionManager implements IConnectionManager {

    private static final String PROP_FILE = "data/DatabaseProperties.properties";
    private SQLServerDataSource ds;

    /**
     * Sets up the data source (object to represent the database).
     *
     * @throws java.io.IOException
     */
    public ConnectionManager() throws IOException {
        setupDataSource();
    }

    private void setupDataSource() throws IOException {
        Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(PROP_FILE));
        ds = new SQLServerDataSource();
        ds.setServerName(databaseProperties.getProperty("Server"));
        ds.setPortNumber(Integer.parseInt(databaseProperties.getProperty("PortNumber")));
        ds.setUser(databaseProperties.getProperty("User"));
        ds.setPassword(databaseProperties.getProperty("Password"));
        ds.setDatabaseName(databaseProperties.getProperty("Database"));
    }

    /**
     * Attempts to establish a connection with the data source.
     *
     * @return A connection to the data source.
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    @Override
    public Connection getConnection() throws SQLServerException {
        return ds.getConnection();
    }

}
