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

    //2.4 hours.
    @Override
    public Map getTasksWithTaskEntries() throws DalException {
        Map<Integer, Task> tasksMap = getTasks();
        List<TaskEntry> taskEntries = getTaskEntries(tasksMap);

        for (Map.Entry<Integer, Task> mapEntry : tasksMap.entrySet()) {
            int key = mapEntry.getKey();
            for (TaskEntry taskEntry : taskEntries) {
                if (taskEntry.getTaskId() == key) {
                    Task task = tasksMap.get(taskEntry.getTaskId());
                    taskEntry.setDescription(task.getDescription()); //OR get it from database with JOIN?
                    task.getTaskEntryList().add(taskEntry);
                }
            }
        }

        return tasksMap;
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
                + "WHERE T.modifiedDate BETWEEN DATEADD(DD, -30, CONVERT(DATE,GETDATE())) AND GETDATE()";

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
                List<TaskEntry> entries = new ArrayList<>();
                LocalDateTime creationTime = rs.getTimestamp("createdDate").toLocalDateTime();

                Task t = new Task(taskId, description, project, entries, creationTime);
                if (!tasks.containsKey(t.getId())) {
                    tasks.put(taskId, t);
                }
            }
            // TODO: Redo exception handling!       
        } catch (SQLException ex) {
            throw new DalException("Could not get the tasks for the Time Tracker. " + ex.getMessage());
        }
        for (Integer name : tasks.keySet()) {
            String key = name.toString();
            String value = tasks.get(name).getDescription();
        }
        return tasks;
    }

    private List<TaskEntry> getTaskEntries(Map tasks) throws DalException {
        List<TaskEntry> entries = new ArrayList<>();
        String sql = "SELECT TE.id, TE.startTime, TE.endTime, TE.taskId "
                + "FROM TaskEntry TE "
                + "WHERE TE.startTime BETWEEN DATEADD(DD, -30, CONVERT(DATE,GETDATE())) AND GETDATE()"
                + "AND "; //taskId = ?;
        String sqlFinal = prepStatement(sql, tasks);
        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sqlFinal);

            Iterator iterator = tasks.entrySet().iterator();
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
                LocalDateTime startTime = rs.getTimestamp("startTime").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("endTime").toLocalDateTime();
                entries.add(new TaskEntry(id, taskId, startTime, endTime));
            }
        } catch (SQLException ex) {
            throw new DalException("Could not get the task entries for the Time Tracker. " + ex.getMessage());
        }
        return entries;
    }

    private String prepStatement(String sql, Map tasks) {
        Map<Integer, Task> taskMap = tasks;
        boolean firstItem = true;
        for (Map.Entry<Integer, Task> entry : taskMap.entrySet()) {
            if (firstItem) {
                sql += "taskId = ? ";
                firstItem = false;
            } else {
                sql += "OR taskId = ? ";
            }
        }
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

    @Override
    public List<Task> getTasksList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
