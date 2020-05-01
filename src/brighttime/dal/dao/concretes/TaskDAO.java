package brighttime.dal.dao.concretes;

import brighttime.be.Task;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.ITaskDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author annem
 */
public class TaskDAO implements ITaskDAO {

    private final IConnectionManager connection;

    public TaskDAO() throws IOException {
        this.connection = new ConnectionManager();
    }

    @Override
    public List<Task> getTasksForCurrentDay(LocalDate date) throws DalException {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT T.id, T.name, T.dateCreated, T.duration, P.name AS projectName, C.name AS clientName "
                + "FROM Task AS T "
                + "JOIN Project AS P "
                + "ON T.projectId = P.id "
                + "JOIN Client AS C "
                + "ON P.clientId = C.id "
                + "WHERE T.dateCreated >= ? AND T.dateCreated < ? "
                + "ORDER BY T.dateCreated DESC";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setDate(2, Date.valueOf(date.plusDays(1)));

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String clientName = rs.getString("clientName");
                String projectName = rs.getString("projectName");
                Time duration = rs.getTime("duration");
                //tasks.add(new Task(id, name, clientName, projectName, duration));
            }
            // TODO: Check exception handling.       
        } catch (SQLException ex) {
            throw new DalException("Could not get the tasks for today. " + ex.getMessage());
        }
        return tasks;
    }

    @Override
    public Task createTask(Task task) throws DalException {
        String sql = "INSERT INTO Task (description, lastUpdate, projectId) "
                + "VALUES (?, SYSDATETIME(), ?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, task.getDescription());
            pstmt.setInt(2, task.getProject().getId());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                task.setId(rs.getInt(1));
            }
            return task;
        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Task> getTasks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
