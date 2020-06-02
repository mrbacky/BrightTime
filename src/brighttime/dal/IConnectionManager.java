package brighttime.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;

/**
 *
 * @author annem
 */
public interface IConnectionManager {

    /**
     * Attempts to establish a connection with the data source.
     *
     * @return A connection to the data source.
     * @throws SQLServerException
     */
    Connection getConnection() throws SQLServerException;

}
