package brighttime.dal.dao.concretes;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.ITaskDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public List<TaskEntry> getTaskEntries() throws DalException {
        List<TaskEntry> entries = new ArrayList<>();
        Map<Integer, Task> taskMap = getTasks();
        String sql = "SELECT CONVERT(DATE, TE.startTime) AS date, "
                + "TE.id, TE.startTime, TE.endTime, TE.taskId "
                + "FROM TaskEntry TE "
                + "WHERE TE.startTime BETWEEN DATEADD(DD, -4, CONVERT(DATE,GETDATE())) AND GETDATE()"
                + "AND ";
        String sqlFinal = prepStatement(sql, taskMap);
        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sqlFinal);

            Iterator iterator = taskMap.entrySet().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Integer key = (Integer) entry.getKey(); //Get key from HashMap and store as local variable.
                pstmt.setInt(i + 1, key);
                i++;
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int taskId = rs.getInt("taskId");
                Task task = taskMap.get(taskId);
                LocalDateTime startTime = rs.getTimestamp("startTime").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("endTime").toLocalDateTime();

                TaskEntry taskEntry = new TaskEntry(id, task, startTime, endTime);

                task.getTaskEntryList().add(taskEntry);
                entries.add(taskEntry);
            }
        } catch (SQLException ex) {
            // TODO: Redo exception handling! The message should be in the controller.    
            throw new DalException("Could not get the task entries for the Time Tracker. " + ex.getMessage());
        }
        return entries;
    }

    private Map getTasks() throws DalException {
        Map<Integer, Task> tasks = new HashMap<>();

        String sql = "SELECT T.id AS taskId, T.description, T.createdDate, "
                + "P.id AS projectId, P.name AS projectName, "
                + "C.id AS clientId, C.name AS clientName "
                + "FROM Task AS T "
                + "JOIN Project AS P "
                + "ON T.projectId = P.id "
                + "JOIN Client AS C "
                + "ON P.clientId = C.id "
                + "WHERE T.modifiedDate BETWEEN DATEADD(DD, -4, CONVERT(DATE,GETDATE())) AND GETDATE()";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                //TODO: Should the clients and projects also be the same instance?
                int clientId = rs.getInt("clientId");
                String clientName = rs.getString("clientName");
                Client client = new Client(clientId, clientName);

                int projectId = rs.getInt("projectId");
                String projectName = rs.getString("projectName");
                Project project = new Project(projectId, projectName, client);

                int taskId = rs.getInt("taskId");
                String description = rs.getString("description");
                List<TaskEntry> entries = new ArrayList<>();
                LocalDateTime creationTime = rs.getTimestamp("createdDate").toLocalDateTime();

                Task t = new Task(taskId, description, project, entries, creationTime);
                if (!tasks.containsKey(t.getId())) {
                    tasks.put(taskId, t);
                }
            }
            // TODO: Redo exception handling! The message should be in the controller.       
        } catch (SQLException ex) {
            throw new DalException("Could not get the tasks for the Time Tracker. " + ex.getMessage());
        }
        return tasks;
    }

    private String prepStatement(String sql, Map tasks) {
        Map<Integer, Task> taskMap = tasks;
        boolean firstItem = true;
        for (Map.Entry<Integer, Task> entry : taskMap.entrySet()) {
            if (firstItem) {
                sql += " (taskId = ?";
                firstItem = false;
            } else {
                sql += " OR taskId = ? ";
            }
        }
        sql += ") ORDER BY date DESC, TE.taskId DESC";
        return sql;
    }

    @Override
    public Task createTask(Task task) throws DalException {
        String sql = "INSERT INTO Task (description, createdDate, modifiedDate, projectId) "
                + "VALUES (?, SYSDATETIME(), SYSDATETIME(), ?)";

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

}
