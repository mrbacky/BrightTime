package brighttime.dal.dao.concretes;

import brighttime.be.EventLog;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import brighttime.dal.dao.interfaces.IEventLogDAO;

/**
 *
 * @author annem
 */
public class EventLogDAO implements IEventLogDAO {

    private final IConnectionManager connection;

    public EventLogDAO() throws IOException {
        this.connection = new ConnectionManager();
    }

    @Override
    public void logEvent(EventLog log) throws DalException {
        //TODO: Not tested.
        String sql = "INSERT INTO EventLog (recordedDate, type, description, username) "
                + "VALUES (SYSDATETIME(), ?, ?, ?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, log.getType().toString());
            pstmt.setString(2, log.getDescription());
            pstmt.setString(3, log.getUsername());
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
    }

}
