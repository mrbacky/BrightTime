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
import java.net.InetAddress;

/**
 *
 * @author annem
 */
public class EventLogDAO implements IEventLogDAO {

    private final IConnectionManager connection;
    private String ipAddress = "";

    public EventLogDAO() throws IOException {
        this.connection = new ConnectionManager();
        ipAddress = InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public void logEvent(EventLog log) throws DalException {
        String sql = "INSERT INTO EventLog (recordedDate, type, description, ipAddress) "
                + "VALUES (SYSDATETIME(), ?, ?, ?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, log.getType().toString());
            pstmt.setString(2, log.getDescription());
            pstmt.setString(3, ipAddress);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
    }

}
