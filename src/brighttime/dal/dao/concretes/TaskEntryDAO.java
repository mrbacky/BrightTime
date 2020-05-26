package brighttime.dal.dao.concretes;

import brighttime.be.EventLog;
import brighttime.be.TaskEntry;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.IEventLogDAO;
import brighttime.dal.dao.interfaces.ITaskEntryDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 *
 * @author annem
 */
public class TaskEntryDAO implements ITaskEntryDAO {

    private final IConnectionManager connection;
    private final IEventLogDAO logDAO;

    public TaskEntryDAO() throws IOException {
        this.connection = new ConnectionManager();
        this.logDAO = new EventLogDAO();
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
            
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Created a task entry for the task \"" + taskEntry.getTask().getDescription()
                    + "\" in the project \"" + taskEntry.getTask().getProject().getName()
                    + "\". Time frame: " + taskEntry.getStartTime() + " - " + taskEntry.getEndTime()));
            
            return taskEntry;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful task entry creation for the task \"" + taskEntry.getTask().getDescription()
                    + "\" in the project \"" + taskEntry.getTask().getProject().getName()
                    + "\". Time frame: " + taskEntry.getStartTime() + " - " + taskEntry.getEndTime() + ". "
                    + Arrays.toString(ex.getStackTrace())));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public TaskEntry updateTaskEntryStartTime(TaskEntry taskEntry) throws DalException {
        String sql = "UPDATE TaskEntry "
                + "SET startTime = ?  "
                + "WHERE id = ? ";

        try (Connection con = connection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, taskEntry.getStartTime());
            ps.setInt(2, taskEntry.getId());
            ps.executeUpdate();
            System.out.println("edited start Time in DAO");
            
            logEvent(taskEntry);
            
            return taskEntry;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful task entry update (start time) for the task \"" + taskEntry.getTask().getDescription()
                    + "\" in the project \"" + taskEntry.getTask().getProject().getName()
                    + "\". Time frame: " + taskEntry.getStartTime() + " - " + taskEntry.getEndTime() + ". "
                    + Arrays.toString(ex.getStackTrace())));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public TaskEntry updateTaskEntryEndTime(TaskEntry taskEntry) throws DalException {
        String sql = "UPDATE TaskEntry "
                + "SET endTime = ?  "
                + "WHERE id = ? ";

        try (Connection con = connection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, taskEntry.getEndTime());
            ps.setInt(2, taskEntry.getId());
            ps.executeUpdate();
            System.out.println("edited endTime in DAO");
            
            logEvent(taskEntry);
            
            return taskEntry;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful task entry update (end time) for the task \"" + taskEntry.getTask().getDescription()
                    + "\" in the project \"" + taskEntry.getTask().getProject().getName()
                    + "\". Time frame: " + taskEntry.getStartTime() + " - " + taskEntry.getEndTime() + ". "
                    + Arrays.toString(ex.getStackTrace())));
            throw new DalException(ex.getMessage());
        }

    }

    private void logEvent(TaskEntry taskEntry) throws DalException {
        //TODO: EventLog check exception handling.        
        logDAO.logEvent(new EventLog(
                EventLog.EventType.INFORMATION,
                "Updated a task entry in the task \"" + taskEntry.getTask().getDescription()
                + "\" in the project \"" + taskEntry.getTask().getProject().getName()
                + "\". Time frame: " + taskEntry.getStartTime() + " - " + taskEntry.getEndTime()));
    }

}
