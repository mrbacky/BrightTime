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
    public Task createTask(Task task) throws DalException {
        String sql = "INSERT INTO Task (description, projectId, createdDate, modifiedDate ) "
                + "VALUES (?, ?, SYSDATETIME(), SYSDATETIME())";

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
    public Map<LocalDate, List<Task>> Tasks() throws DalException {
        try {
            dateMap = new HashMap<>();

            getEntries();

            List<Task> emptyTasksList = getTasksWithoutEntries();
            for (Task emptyTask : emptyTasksList) {
                LocalDate date = emptyTask.getCreationTime().toLocalDate();
                if (!dateMap.containsKey(date)) {
                    List<Task> list = new ArrayList<>();
                    list.add(emptyTask);
                    dateMap.put(date, list);
                }
                if (dateMap.containsKey(date)) {
                    List<Task> tasks = dateMap.get(date);
                    if (!tasks.contains(emptyTask)) {
                        tasks.add(0, emptyTask);
                    }
                }
            }

            return dateMap;
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    /**
     * Gets the task entries logged between the current day and 30 days ago.
     * Uses the getTasks() to set each TaskEntry's connection to its Task. Also,
     * adds the Task to the dateMap (data structure) for the View, so the Task
     * gets added to a date to show it has entries for the particular date.
     *
     * @return A list of all entries.
     * @throws DalException
     */
    private List<TaskEntry> getEntries() throws DalException {
        List<TaskEntry> entries = new ArrayList<>();

        Map<Integer, Task> map = getTasks();

        String sql = "SELECT CONVERT(DATE, TE.startTime) AS date, "
                + "TE.id, TE.startTime, TE.endTime, TE.taskId "
                + "FROM TaskEntry TE "
                + "WHERE TE.startTime BETWEEN DATEADD(DD, -30, CONVERT(DATE,GETDATE())) AND GETDATE()"
                + "AND ";
        String sqlFinal = prepStatement(sql, map);
        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sqlFinal);

            Iterator iterator = map.entrySet().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Integer key = (Integer) entry.getKey();
                pstmt.setInt(i + 1, key);
                i++;
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int taskId = rs.getInt("taskId");
                Task task = map.get(taskId);
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
            throw new DalException(ex.getMessage());
        }
        return entries;
    }

    /**
     * Gets the tasks which have been modified between the current day and 30
     * days ago and stores it in a HashMap.
     *
     * @return A map with a Task instance as a value and its taskId as the key.
     * @throws DalException
     */
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
                + "WHERE T.modifiedDate BETWEEN DATEADD(DD, -30, CONVERT(DATE,GETDATE())) AND GETDATE()";

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
        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
        return taskMap;
    }

    /**
     * Prepares the rest of the SQL statement, which will change depending on
     * the number of tasks.
     *
     * @param sql The constant part of the SQL statement.
     * @param tasks The map of tasks.
     * @return The complete SQL query.
     */
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
        sql += ") ORDER BY TE.startTime DESC";
        return sql;
    }

    /**
     * Gets the tasks logged between the current day and 30 days ago, which do
     * not have any entries.
     *
     * @return A list of tasks.
     * @throws DalException
     */
    private List<Task> getTasksWithoutEntries() throws DalException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id "
                + "  FROM Task "
                + "  WHERE createdDate = modifiedDate "
                + "  AND createdDate BETWEEN DATEADD(DD, -30, CONVERT(DATE,GETDATE())) AND GETDATE()"
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

}
