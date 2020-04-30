package brighttime.dal.dao.concretes;

import brighttime.be.TaskEntry;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.ITaskEntryDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author annem
 */
public class TaskEntryDAO implements ITaskEntryDAO {

    private final IConnectionManager connection;

    public TaskEntryDAO() throws IOException {
        this.connection = new ConnectionManager();
    }

    @Override
    public TaskEntry createTaskEntry(TaskEntry taskEntry) throws DalException {
        String sql = "INSERT INTO TaskEntry(startTime, endTime, taskId) "
                + "VALUES (?, ?, ?) "
                + "UPDATE Task "
                + "SET lastUpdate = SYSDATETIME() "
                + "WHERE id = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setObject(1, taskEntry.getStartTime());
            pstmt.setObject(2, taskEntry.getEndTime());
            pstmt.setInt(3, taskEntry.getTaskId());
            pstmt.setInt(4, taskEntry.getTaskId());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                taskEntry.setId(rs.getInt(1));
            }
            return taskEntry;
        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
    }

}
