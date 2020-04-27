package brighttime.dal.dao.concretes;

import brighttime.be.Task;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.ITaskDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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
    public List<Task> getTasksForCurrentDay() throws DalException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT T.id, T.name, T.dateCreated, T.duration, P.name AS projectName, C.name AS clientName "
                + "FROM Task AS T "
                + "JOIN Project AS P "
                + "ON T.projectId = P.id "
                + "JOIN Client AS C "
                + "ON P.clientId = C.id "
                + "WHERE T.dateCreated = CONVERT(DATE,GETDATE())";
        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String clientName = rs.getString("clientName");
                String projectName = rs.getString("projectName");
                Time duration = rs.getTime("duration");
                tasks.add(new Task(id, name, clientName, projectName, duration));
            }
            // TODO: Check exception handling.       
        } catch (SQLException ex) {
            throw new DalException("Could not get the tasks for today. " + ex.getMessage());
        }
        return tasks;
    }

}
