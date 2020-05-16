package brighttime.dal.dao.concretes;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskBase;
import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
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
    private Map<LocalDate, List<TaskConcrete1>> dateMap;
    private Map<Integer, TaskConcrete1> taskMap;
    private TaskConcrete1 task;

    public TaskDAO() throws IOException {
        this.connection = new ConnectionManager();
    }

    @Override
    public TaskConcrete1 createTask(TaskConcrete1 task) throws DalException {
        String sql = "INSERT INTO Task (description, createdDate, modifiedDate, projectId, billability, userId) "
                + "VALUES (?, SYSDATETIME(), SYSDATETIME(), ?, ?, ?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, task.getDescription());
            pstmt.setInt(2, task.getProject().getId());

            if (task.getBillability() == TaskBase.Billability.BILLABLE) {
                pstmt.setString(3, String.valueOf('B'));
            } else if (task.getBillability() == TaskBase.Billability.NON_BILLABLE) {
                pstmt.setString(3, String.valueOf('N'));
            }
            pstmt.setInt(4, task.getUser().getId());
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
    public Map<LocalDate, List<TaskConcrete1>> Tasks() throws DalException {
        try {
            dateMap = new HashMap<>();

            getEntries();

            List<TaskConcrete1> emptyTasksList = getTasksWithoutEntries();
            if (!emptyTasksList.isEmpty()) {
                for (TaskConcrete1 emptyTask : emptyTasksList) {
                    LocalDate date = emptyTask.getCreationTime().toLocalDate();
                    if (!dateMap.containsKey(date)) {
                        List<TaskConcrete1> list = new ArrayList<>();
                        list.add(emptyTask);
                        dateMap.put(date, list);
                    }
                    if (dateMap.containsKey(date)) {
                        List<TaskConcrete1> tasks = dateMap.get(date);
                        if (!tasks.contains(emptyTask)) {
                            tasks.add(0, emptyTask);
                        }
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

        Map<Integer, TaskConcrete1> map = getTasks();

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
                TaskConcrete1 taskFromMap = map.get(taskId);
                LocalDateTime startTime = rs.getTimestamp("startTime").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("endTime").toLocalDateTime();

                TaskEntry taskEntry = new TaskEntry(id, taskFromMap, startTime, endTime);

                taskFromMap.getTaskEntryList().add(taskEntry);
                entries.add(taskEntry);

                LocalDate date = startTime.toLocalDate();
                if (!dateMap.containsKey(date)) {
                    List<TaskConcrete1> list = new ArrayList<>();
                    list.add(taskEntry.getTask());
                    dateMap.put(date, list);
                }
                if (dateMap.containsKey(date)) {
                    List<TaskConcrete1> list = dateMap.get(date);
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

        String sql = "SELECT T.id AS taskId, T.description, T.modifiedDate, T.billability, "
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

                String billability = rs.getString("billability");

                if (billability.equals("B")) {
                    task = new TaskConcrete1(taskId, description, TaskBase.Billability.BILLABLE, project, entries, creationTime);
                } else if (billability.equals("N")) {
                    task = new TaskConcrete1(taskId, description, TaskBase.Billability.NON_BILLABLE, project, entries, creationTime);
                }

                if (!taskMap.containsKey(task.getId())) {
                    taskMap.put(taskId, task);
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
    private String prepStatement(String sql, Map<Integer, TaskConcrete1> tasks) {
        boolean firstItem = true;
        for (Map.Entry<Integer, TaskConcrete1> entry : tasks.entrySet()) {
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
    private List<TaskConcrete1> getTasksWithoutEntries() throws DalException {
        List<TaskConcrete1> tasks = new ArrayList<>();
        String sql = "SELECT id "
                + "  FROM Task "
                + "  WHERE createdDate = modifiedDate "
                + "  AND createdDate BETWEEN DATEADD(DD, -30, CONVERT(DATE,GETDATE())) AND GETDATE()"
                + "  ORDER BY createdDate DESC";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TaskConcrete1 taskWithoutEntry = taskMap.get(rs.getInt("id"));
                tasks.add(taskWithoutEntry);
            }

        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
        return tasks;
    }

    @Override
    public List<TaskConcrete2> getAllTasks() throws DalException {
        List<TaskConcrete2> allTasks = new ArrayList<>();
        //TODO: Make one method like getAllTasksFiltered(), so there is only one database call. 
        String sql = "SELECT A2.id, A2.description, "
                + "	SUM(A2.totalDuration) AS totalDuration, "
                + "	A2.billability, A1.clientRate, A1.projectRate "
                + "FROM "
                + "	( "
                + "	SELECT C.hourlyRate AS clientRate, P.hourlyRate AS projectRate, P.id AS projectId "
                + "	FROM Client C "
                + "	JOIN Project P "
                + "	ON C.id = P.clientId "
                + "	) "
                + "	AS A1 "
                + "JOIN "
                + "	( "
                + "	SELECT T.id, T.description, "
                + "	(DATEDIFF(SECOND,TE.startTime,TE.endTime)) AS totalDuration, "
                + "	T.billability, T.projectId "
                + "	FROM Task T "
                + "	LEFT JOIN TaskEntry TE "
                + "	ON T.id = TE.taskId "
                + "	) "
                + "	AS A2 "
                + "ON A1.projectId = A2.projectId "
                + "GROUP BY A2.id, A2.description, A2.billability, "
                + "	A1.clientRate, A1.projectRate "
                + "ORDER BY A2.description";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                //TODO: Is it necessary to get unused info?
                String rsBillability = rs.getString("billability");

                int rate;
                TaskBase.Billability billability;

                if (rsBillability.equals("B")) {
                    billability = TaskBase.Billability.BILLABLE;
                    rate = rs.getInt("projectRate");
                    if (rate == 0) {
                        rate = rs.getInt("clientRate");
                    };
                } else {
                    billability = TaskBase.Billability.NON_BILLABLE;
                    rate = 0;
                }

                allTasks.add(new TaskConcrete2(rs.getInt("id"),
                        rs.getString("description"),
                        billability,
                        rs.getInt("totalDuration"),
                        rate
                ));
            }
        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
        return allTasks;
    }

    @Override
    public List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws DalException {
        List<TaskConcrete2> filtered = new ArrayList<>();

        String sql = "SELECT A2.id, A2.description, "
                + "	SUM(A2.totalDuration) AS totalDuration, "
                + "	A2.billability, A1.clientRate, A1.projectRate "
                + "FROM "
                + "	( "
                + "	SELECT C.hourlyRate AS clientRate, P.hourlyRate AS projectRate, P.id AS projectId "
                + "	FROM Client C "
                + "	JOIN Project P "
                + "	ON C.id = P.clientId "
                + "	) "
                + "	AS A1 "
                + "JOIN "
                + "	( "
                + "	SELECT T.id, T.description, "
                + "	(DATEDIFF(SECOND,TE.startTime,TE.endTime)) AS totalDuration, "
                + "	T.billability, T.projectId "
                + "	FROM Task T "
                + "	JOIN TaskEntry TE "
                + "	ON T.id = TE.taskId "
                + "	WHERE ";

        String sqlFinal = buildSql(sql, filter);

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sqlFinal);

            int i = 0;

            if (filter.getUser() != null) {
                pstmt.setInt(++i, filter.getUser().getId());
            }

            if (filter.getProject() != null) {
                pstmt.setInt(++i, filter.getProject().getId());
            }

            if (filter.getStartDate() != null && filter.getEndDate() != null) {
                pstmt.setDate(++i, Date.valueOf(filter.getStartDate()));
                pstmt.setDate(++i, Date.valueOf(filter.getEndDate().plusDays(1)));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                //TODO: Is it necessary to get unused info?

                String rsBillability = rs.getString("billability");

                int rate;
                TaskBase.Billability billability;

                if (rsBillability.equals("B")) {
                    billability = TaskBase.Billability.BILLABLE;
                    rate = rs.getInt("projectRate");
                    if (rate == 0) {
                        rate = rs.getInt("clientRate");
                    };
                } else {
                    billability = TaskBase.Billability.NON_BILLABLE;
                    rate = 0;
                }

                filtered.add(new TaskConcrete2(rs.getInt("id"),
                        rs.getString("description"),
                        billability,
                        rs.getInt("totalDuration"),
                        rate
                ));
            }
        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
        return filtered;
    }

    private String buildSql(String sql, Filter filter) {
        if (filter.getUser() != null) {
            sql += "T.userId = ? ";
        }
        if (filter.getProject() != null) {
            if (filter.getUser() != null) {
                sql += "AND ";
            }
            sql += "T.projectId = ? ";
        }

        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            if (filter.getUser() != null || filter.getProject() != null) {
                sql += "AND ";
            }
            sql += "TE.startTime BETWEEN ? AND ? ";
        }
        sql += "	) AS A2 "
                + "ON A1.projectId = A2.projectId "
                + "GROUP BY A2.id, A2.description, A2.billability, "
                + "	A1.clientRate, A1.projectRate "
                + "ORDER BY A2.description";
        return sql;
    }

}
