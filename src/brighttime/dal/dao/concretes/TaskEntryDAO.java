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
                + "SET modifiedDate = SYSDATETIME() "
                + "WHERE id = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setObject(1, taskEntry.getStartTime());
            pstmt.setObject(2, taskEntry.getEndTime());
            pstmt.setInt(3, taskEntry.getTask().getId());
            pstmt.setInt(4, taskEntry.getTask().getId());
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

    @Override
    public TaskEntry updateTaskEntryStartTime(TaskEntry taskEntry) {
        String sql = "UPDATE TaskEntry "
                + "SET startTime = ?  "
                + "WHERE id = ? ";

        try (Connection con = connection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, taskEntry.getStartTime());
            ps.setInt(2, taskEntry.getId());
            ps.executeUpdate();

        } catch (Exception e) {
        }

        return taskEntry;
    }

    @Override
    public TaskEntry updateTaskEntryEndTime(TaskEntry taskEntry) {
        String sql = "UPDATE TaskEntry "
                + "SET endTime = ?  "
                + "WHERE id = ? ";

        try (Connection con = connection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, taskEntry.getEndTime());
            ps.setInt(2, taskEntry.getId());
            ps.executeUpdate();

        } catch (Exception e) {
        }

        return taskEntry;
    }

}
