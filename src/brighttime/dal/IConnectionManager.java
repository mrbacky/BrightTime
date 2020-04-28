package brighttime.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;

/**
 *
 * @author annem
 */
public interface IConnectionManager {

    Connection getConnection() throws SQLServerException;

}
