package brighttime.dal.dao.concretes;

import brighttime.dal.ConnectionManager;
import brighttime.dal.IConnectionManager;
import java.io.IOException;

/**
 *
 * @author annem
 */
public class LogDAO {

    private final IConnectionManager connection;

    public LogDAO() throws IOException {
        this.connection = new ConnectionManager();
    }

    public void logActions() {
    }

}
