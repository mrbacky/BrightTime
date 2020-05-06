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
import java.time.LocalDate;
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
    private Map<LocalDate, List<Task>> dateMap;
    private Map<Integer, Task> taskMap;

    public TaskDAO() throws IOException {
        this.connection = new ConnectionManager();
    }

    @Override
    public Map<LocalDate, List<Task>> Tasks() throws DalException {
        dateMap = new HashMap();
        getEntries();
        List<Task> newTasks = findTasksWithoutEntries();
        for (Task newTask : newTasks) {
            LocalDate date = newTask.getCreationTime().toLocalDate();
            if (!dateMap.containsKey(date)) {
                List<Task> list = new ArrayList<>();
                list.add(newTask);
                dateMap.put(date, list);
            }
            if (dateMap.containsKey(date)) {
                List<Task> lst = dateMap.get(date);
                if (!lst.contains(newTask)) {
                    List<Task> list = dateMap.get(date);
                    list.add(newTask);
                }
            }
        }
        return dateMap;
    }

    private List<TaskEntry> getEntries() throws DalException {
        List<TaskEntry> entries = new ArrayList<>();
        Map<Integer, Task> tasks = getTasks();
        String sql = "SELECT CONVERT(DATE, TE.startTime) AS date, "
                + "TE.id, TE.startTime, TE.endTime, TE.taskId "
                + "FROM TaskEntry TE "
                + "WHERE TE.startTime BETWEEN DATEADD(DD, -4, CONVERT(DATE,GETDATE())) AND GETDATE()"
                + "AND ";
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
                Task task = tasks.get(taskId);
                LocalDateTime startTime = rs.getTimestamp("startTime").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("endTime").toLocalDateTime();

                TaskEntry taskEntry = new TaskEntry(id, task, startTime, endTime);

                task.getTaskEntryList().add(taskEntry);
                entries.add(taskEntry);

                LocalDate date = startTime.toLocalDate();

                if (!dateMap.containsKey(date)) {
                    List<Task> list = new ArrayList<>();
                    list.add(taskEntry.getTask());
                    dateMap.put(date, list);
                }
                if (dateMap.containsKey(date)) {
                    List<Task> list = dateMap.get(date);
                    if (!list.contains(taskEntry.getTask())) {
                        list.add(taskEntry.getTask());
                    }
                }
            }
        } catch (SQLException ex) {
            // TODO: Redo exception handling! The message should be in the controller.    
            throw new DalException("Could not get the task entries for the Time Tracker. " + ex.getMessage());
        }
        return entries;
    }

    private Map getTasks() throws DalException {
        taskMap = new HashMap<>();

        String sql = "SELECT T.id AS taskId, T.description, T.modifiedDate, "
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
                LocalDateTime creationTime = rs.getTimestamp("modifiedDate").toLocalDateTime();

                Task t = new Task(taskId, description, project, entries, creationTime);
                if (!taskMap.containsKey(t.getId())) {
                    taskMap.put(taskId, t);
                }
            }
            // TODO: Redo exception handling! The message should be in the controller.       
        } catch (SQLException ex) {
            throw new DalException("Could not get the tasks for the Time Tracker. " + ex.getMessage());
        }
        return taskMap;
    }

    private String prepStatement(String sql, Map<Integer, Task> tasks) {
        boolean firstItem = true;
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
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

    private List<Task> findTasksWithoutEntries() throws DalException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id "
                + "  FROM Task "
                + "  WHERE createdDate = modifiedDate "
                + "  AND createdDate BETWEEN DATEADD(DD, -4, CONVERT(DATE,GETDATE())) AND GETDATE()"
                + "  ORDER BY createdDate DESC";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Task task = taskMap.get(rs.getInt("id"));
                tasks.add(task);
            }

        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
        return tasks;
    }

    @Override
    public List<TaskEntry> getTaskEntries() throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
