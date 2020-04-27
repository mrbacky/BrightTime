package brighttime.dal.dao.concretes;

import brighttime.be.Task;
import brighttime.dal.ConnectionManager;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.ITaskDAO;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public List<Task> getTasksForCurrentDay() {
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

        } catch (SQLServerException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Task task : tasks) {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getProjectName());
            System.out.println(task.getClientName());
            System.out.println(task.getDuration());
        }
        return tasks;
    }

}
