package brighttime.dal.dao;

import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.net.InetAddress;

/**
 *
 * @author annem
 */
public class EventLogDAO implements IEventLogDAO {

    private final IConnectionManager connection;
    private String ipAddress = "";

    public enum EventType {
        INFORMATION, ERROR
    }

    public EventLogDAO() throws IOException {
        this.connection = new ConnectionManager();
        ipAddress = InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public void logEvent(EventType type, String description) throws DalException {
        String sql = "INSERT INTO EventLog (recordedDate, type, description, ipAddress) "
                + "VALUES (SYSDATETIME(), ?, ?, ?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, type.toString());
            pstmt.setString(2, description);
            pstmt.setString(3, ipAddress);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
    }

}
