package brighttime.dal.dao.concretes;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.ITaskDAO;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
    public List<Task> getTasks() throws DalException {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT T.id AS taskId, T.description, "
                + "P.id AS projectId, P.name AS projectName, "
                + "C.id AS clientId, C.name AS clientName "
                + "FROM Task AS T "
                + "JOIN Project AS P "
                + "ON T.projectId = P.id "
                + "JOIN Client AS C "
                + "ON P.clientId = C.id "
                + "WHERE T.lastUpdate BETWEEN DATEADD(DD, -5, CONVERT(DATE,GETDATE())) AND GETDATE()";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int clientId = rs.getInt("clientId");
                String clientName = rs.getString("clientName");
                Client client = new Client(clientId, clientName);

                int projectId = rs.getInt("projectId");
                String projectName = rs.getString("projectName");
                Project project = new Project(projectId, projectName, client);

                int taskId = rs.getInt("taskId");
                String description = rs.getString("description");
                List<TaskEntry> entries = getTaskEntries(taskId, description);

                tasks.add(new Task(taskId, description, project, entries));
            }
            // TODO: Check exception handling.       
        } catch (SQLException ex) {
            throw new DalException("Could not get the tasks for today. " + ex.getMessage());
        }
        for (Task task : tasks) {
            System.out.println("test: " + task.getId());
            System.out.println("test: " + task.getDescription());
            System.out.println("test: " + task.getProject().getName());
            System.out.println("test: " + task.getProject().getClient().getName());
            List<TaskEntry> entries = task.getTaskEntryList();
            for (TaskEntry entry : entries) {
                System.out.println("entry id: " + entry.getId());
                System.out.println("entry start: " + entry.getStartTime());
                System.out.println("entry end: " + entry.getEndTime());
            }
        }
        return tasks;
    }

    private List<TaskEntry> getTaskEntries(int taskId, String description) {
        List<TaskEntry> entries = new ArrayList<>();
        String sql = "SELECT TE.id, TE.startTime, TE.endTime "
                + "FROM TaskEntry TE "
                + "WHERE taskId = ?;";
        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                LocalDateTime startTime = rs.getTimestamp("startTime").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("endTime").toLocalDateTime();
                entries.add(new TaskEntry(id, description, startTime, endTime));
            }
        } catch (SQLServerException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entries;
    }
}
