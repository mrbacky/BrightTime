package brighttime.dal.dao.concretes;

import brighttime.be.Client;
import brighttime.be.EventLog;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskBase;
import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.IEventLogDAO;
import brighttime.dal.dao.interfaces.ITaskDAO;
import brighttime.dal.dao.interfaces.ITaskEntryDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author annem
 */
public class TaskDAO implements ITaskDAO {

    private final IConnectionManager connection;
    private final IEventLogDAO logDAO;
    private final ITaskEntryDAO taskEntryDAO;

    public TaskDAO() throws IOException {
        this.connection = new ConnectionManager();
        this.logDAO = new EventLogDAO();
        this.taskEntryDAO = new TaskEntryDAO();
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
                int taskId = rs.getInt(1);
                task.setId(taskId);
                if (task.getTaskEntryList() != null) {
                    taskEntryDAO.createTaskEntry(task.getTaskEntryList().get(0));
                }
            }

            logDAO.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Created the task in the project \"" + task.getProject().getName() + "\": "
                    + task.getDescription() + "."));

            return task;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful task creation for \"" + task.getProject().getName() + "\": "
                    + task.getDescription() + ". " + Arrays.toString(ex.getStackTrace())));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public Map<LocalDate, List<TaskConcrete1>> getAllTasksWithEntries(User user, LocalDate start, LocalDate end) throws DalException {
        //TODO: The method is IN PROGRESS.
        Map<Integer, TaskConcrete1> taskMap = new HashMap<>();
        Map<LocalDate, List<TaskConcrete1>> dateMap = new HashMap<>();

        String sql = "SELECT	T.id AS taskId, T.description, T.modifiedDate, T.billability, "
                + "		P.id AS projectId, P.name AS projectName, "
                + "		C.id AS clientId, C.name AS clientName, "
                + "		TE.id AS taskEntryId, TE.startTime, TE.endTime "
                + "FROM TaskEntry TE "
                + "JOIN Task T ON TE.taskId = T.id "
                + "JOIN Project P ON T.projectId = P.id "
                + "JOIN Client C ON P.clientId = C.id "
                + "WHERE	T.userId = ? "
                + "		AND ? <= TE.startTime "
                + "		AND TE.startTime < ? "
                + " "
                + "UNION "
                + " "
                + "SELECT	T.id AS taskId, T.description, T.modifiedDate, T.billability, "
                + "		P.id AS projectId, P.name AS projectName, "
                + "		C.id AS clientId, C.name AS clientName, "
                + "		taskEntryId = NULL, startTime = NULL, endTime = NULL "
                + "FROM Task T "
                + "JOIN Project P ON T.projectId = P.id "
                + "JOIN Client C ON P.clientId = C.id "
                + "WHERE	T.userId = ? "
                + "		AND ? <= T.modifiedDate "
                + "		AND T.modifiedDate < ? "
                + "		AND NOT EXISTS (SELECT * FROM TaskEntry TE WHERE T.id = TE.taskId) "
                + " "
                + "ORDER BY startTime DESC, modifiedDate DESC";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, user.getId());
            pstmt.setDate(2, Date.valueOf(start));
            pstmt.setDate(3, Date.valueOf(end.plusDays(1)));
            pstmt.setInt(4, user.getId());
            pstmt.setDate(5, Date.valueOf(start));
            pstmt.setDate(6, Date.valueOf(end.plusDays(1)));

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                //Create task from ResultSet
                int taskId = rs.getInt("taskId");

                LocalDateTime modifiedDate = rs.getTimestamp("modifiedDate").toLocalDateTime();

                TaskConcrete1 newTask;

                if (!taskMap.containsKey(taskId)) {
                    Client client = new Client(rs.getInt("clientId"), rs.getString("clientName"));
                    Project project = new Project(rs.getInt("projectId"), rs.getString("projectName"), client);

                    TaskBase.Billability billability;

                    if (rs.getString("billability").equals("B")) {
                        billability = TaskBase.Billability.BILLABLE;
                    } else {
                        billability = TaskBase.Billability.NON_BILLABLE;
                    }

                    List<TaskEntry> entryList = new ArrayList();

                    newTask = new TaskConcrete1(
                            taskId,
                            rs.getString("description"),
                            project,
                            billability,
                            entryList,
                            modifiedDate,
                            user);
                    taskMap.put(taskId, newTask);
//                    newTask = new TaskConcrete1(
//                            taskId,
//                            rs.getString("description"),
//                            project,
//                            billability,
//                            entryList,
//                            modifiedDate);
//                    taskMap.put(taskId, newTask);

                } else {
                    newTask = taskMap.get(taskId);
                }

                //Create task entry from ResultSet
                LocalDateTime startTime = null;
                if (rs.getTimestamp("startTime") != null) {
                    startTime = rs.getTimestamp("startTime").toLocalDateTime();
                }

                int taskEntryId = rs.getInt("taskEntryId");
                if (taskEntryId > 0) {
                    if (rs.getTimestamp("endTime") != null) {
                        LocalDateTime endTime = rs.getTimestamp("endTime").toLocalDateTime();
                        TaskEntry newEntry = new TaskEntry(taskEntryId, newTask, startTime, endTime);
                        newTask.getTaskEntryList().add(newEntry);
                    }
                }

                LocalDate dateOfTask;
                if (startTime != null) {
                    dateOfTask = startTime.toLocalDate();
                } else {
                    dateOfTask = modifiedDate.toLocalDate();
                }

                dateMap = buildDateMap(dateMap, newTask, dateOfTask);
            }

            logDAO.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Loaded all tasks for the Time Tracker."));

            return dateMap;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful getting tasks for the Time Tracker. " + Arrays.toString(ex.getStackTrace())));
            throw new DalException(ex.getMessage());
        }
    }

    /**
     * Builds the data structure (map) which is needed to display the tasks in
     * the TimeTracker View. *
     *
     * @return A map with a list of tasks (containing entries) for each day.
     */
    private Map<LocalDate, List<TaskConcrete1>> buildDateMap(Map<LocalDate, List<TaskConcrete1>> map,
            TaskConcrete1 newTask, LocalDate date) {
        if (!map.containsKey(date)) {
            List<TaskConcrete1> taskList = new ArrayList<>();
            taskList.add(newTask);
            map.put(date, taskList);
        } else {
            List<TaskConcrete1> list = map.get(date);
            if (!list.contains(newTask)) {
                list.add(newTask);
            }
        }
        return map;
    }

    @Override
    public List<TaskConcrete2> getAllTasks() throws DalException {
        List<TaskConcrete2> allTasks = new ArrayList<>();

        String sql = "SELECT A2.id, A2.description, "
                + "	A1.clientId, A1.clientName, "
                + "	A1.projectId, A1.projectName, "
                + "	SUM(A2.totalDuration) AS totalDuration, "
                + "	A2.billability, A1.clientRate, A1.projectRate "
                + "FROM "
                + "	( "
                + "	SELECT C.id AS clientId, C.name AS clientName, c.hourlyRate AS clientRate, "
                + "	P.hourlyRate AS projectRate, P.id AS projectId, P.name AS projectName "
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
                + "	A1.clientId, A1.clientName, "
                + "	A1.projectId, A1.projectName, "
                + "	A1.clientRate, A1.projectRate "
                + "ORDER BY A2.description";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
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

                Client client = new Client(rs.getInt("clientId"), rs.getString("clientName"));
                Project project = new Project(rs.getInt("projectId"), rs.getString("projectName"), client);

                allTasks.add(new TaskConcrete2(rs.getInt("id"),
                        rs.getString("description"),
                        project,
                        billability,
                        rs.getInt("totalDuration"),
                        rate
                ));
            }

            logDAO.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Loaded all tasks for the Overview."));

            return allTasks;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful getting tasks for the Overview. " + Arrays.toString(ex.getStackTrace())));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws DalException {
        List<TaskConcrete2> filtered = new ArrayList<>();

        String sql = "SELECT A2.id, A2.description, "
                + "	A1.clientId, A1.clientName, "
                + "	A1.projectId, A1.projectName, "
                + "	SUM(A2.totalDuration) AS totalDuration, "
                + "	A2.billability, A1.clientRate, A1.projectRate "
                + "FROM "
                + "	( "
                + "	SELECT C.id AS clientId, C.name AS clientName, c.hourlyRate AS clientRate, "
                + "	P.hourlyRate AS projectRate, P.id AS projectId, P.name AS projectName "
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

                Client client = new Client(rs.getInt("clientId"), rs.getString("clientName"));
                Project project = new Project(rs.getInt("projectId"), rs.getString("projectName"), client);

                filtered.add(new TaskConcrete2(rs.getInt("id"),
                        rs.getString("description"),
                        project,
                        billability,
                        rs.getInt("totalDuration"),
                        rate
                ));
            }

            logDAO.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Loaded all filtered tasks for the Overview."));

            return filtered;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful getting filtered tasks for the Overview. " + Arrays.toString(ex.getStackTrace())));
            throw new DalException(ex.getMessage());
        }
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
                + "	A1.clientId, A1.clientName, "
                + "	A1.projectId, A1.projectName, "
                + "	A1.clientRate, A1.projectRate "
                + "ORDER BY A2.description";
        return sql;
    }

    @Override
    public boolean hasTask(User user) throws DalException {
        boolean hasTask = false;

        String sql = "SELECT TOP (1) id "
                + "FROM Task "
                + "WHERE userId = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, user.getId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                hasTask = true;
            }

            logDAO.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Checked if the user has a task."));
        } catch (SQLException ex) {
            //TODO: EventLog, edit message.
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful in checking if the user has a task. " + Arrays.toString(ex.getStackTrace())));
            throw new DalException(ex.getMessage());
        }
        return hasTask;
    }

}
