package brighttime.dal.dao.mockDAO;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskBase;
import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
import brighttime.dal.DalException;
import brighttime.dal.dao.interfaces.ITaskDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rado
 */
public class MockTaskDAO implements ITaskDAO {

    private List<TaskEntry> taskEntries1 = new ArrayList<>();
    private List<TaskEntry> taskEntries2 = new ArrayList<>();
    private List<TaskEntry> taskEntries3 = new ArrayList<>();

    private List<TaskBase> taskList = new ArrayList<>();

    private Map<LocalDate, List<TaskBase>> taskMap = new HashMap<>();

    private Project brightTime;
    private Client grumsen;

    public MockTaskDAO() {
//        createClient();
//        createProject();
//        createInitialTasks();
//        createUITaskEntries();
//        createInitialTaskMap();
    }

    private void createClient() {
        grumsen = new Client(0, "Grumsen Development", 1000);
    }

    private void createProject() {
        brightTime = new Project(0, "BrightTime", grumsen);
    }

    private void createUITaskEntries() {

//        taskEntries1.add(new TaskEntry(0, LocalDateTime.parse("2020-04-25T02:00:00"), LocalDateTime.parse("2020-05-25T09:00:00")));
//        taskEntries1.add(new TaskEntry(1, LocalDateTime.parse("2020-04-25T10:00:00"), LocalDateTime.parse("2020-04-25T11:20:00")));
//
//        taskEntries2.add(new TaskEntry(2, LocalDateTime.parse("2020-04-26T15:00:00"), LocalDateTime.parse("2020-04-28T18:20:00")));
//        taskEntries2.add(new TaskEntry(3, LocalDateTime.parse("2020-04-26T18:30:00"), LocalDateTime.parse("2020-04-28T18:40:00")));
//        taskEntries2.add(new TaskEntry(4, LocalDateTime.parse("2020-04-26T20:00:00"), LocalDateTime.parse("2020-04-28T21:20:00")));
//        taskEntries2.add(new TaskEntry(5, LocalDateTime.parse("2020-04-26T22:00:00"), LocalDateTime.parse("2020-04-28T23:20:00")));
//
//        taskEntries3.add(new TaskEntry(6, LocalDateTime.parse("2020-04-28T15:00:00"), LocalDateTime.parse("2020-04-28T18:20:00")));
    }

    public void createInitialTasks() {
//        taskList.add(new Task(0, "create UI", brightTime, taskEntries1, LocalDateTime.parse("2020-02-25T02:00:00")));
//        taskList.add(new Task(1, "create UI", brightTime, taskEntries1, LocalDateTime.parse("2020-02-25T02:00:00")));
//        taskList.add(new Task(2, "create UI", brightTime, taskEntries3, LocalDateTime.parse("2020-02-01T02:00:00")));

    }

    public List<TaskBase> getTasksList() {
//        return taskList;
        return null;
    }

    @Override
    public TaskConcrete1 createTask(TaskConcrete1 task) throws DalException {
        return task;
    }

    private void createInitialTaskMap() {
        taskMap.put(LocalDate.parse("2020-04-1"), taskList);

    }

    public Map getTasksWithTaskEntries() throws DalException {
        return taskMap;
    }

    @Override
    public List<TaskConcrete2> getAllTasks() throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<LocalDate, List<TaskConcrete1>> getAllTasksWithEntries(User user) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasTask(User user) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
