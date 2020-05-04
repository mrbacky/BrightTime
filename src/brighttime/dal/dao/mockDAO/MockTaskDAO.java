package brighttime.dal.dao.mockDAO;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.dal.DalException;
import brighttime.dal.dao.interfaces.ITaskDAO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.util.Duration;

/**
 *
 * @author rado
 */
public class MockTaskDAO implements ITaskDAO {

    private List<TaskEntry> taskEntries1 = new ArrayList<>();
    private List<TaskEntry> taskEntries2 = new ArrayList<>();
    private List<TaskEntry> taskEntries3 = new ArrayList<>();
    private List<Task> taskList = new ArrayList<>();

    private Project brightTime;
    private Client grumsen;

    public MockTaskDAO() {
        createClient();
        createProject();

        createUITaskEntries();
        sendEmailTaskEntries();
        eatTaskEntries();

        createInitialTasks();
    }

    private void createClient() {
        grumsen = new Client(0, "Grumsen Development");
    }

    private void createProject() {
        brightTime = new Project(0, "BrightTime", grumsen);
    }

    private void createUITaskEntries() {

//        taskEntries1.add(new TaskEntry(0, "create UI for App", LocalDateTime.parse("2020-04-28T02:00:00"), LocalDateTime.parse("2020-04-28T09:00:00")));
//        taskEntries1.add(new TaskEntry(1, "create UI for App", LocalDateTime.parse("2020-04-28T10:00:00"), LocalDateTime.parse("2020-04-28T11:20:00")));
//        taskEntries1.add(new TaskEntry(2, "create UI for App", LocalDateTime.parse("2020-04-28T15:00:00"), LocalDateTime.parse("2020-04-28T18:20:00")));
    }

    private void sendEmailTaskEntries() {
//        taskEntries2.add(new TaskEntry(3, "send email", LocalDateTime.parse("2020-04-28T18:45:00"), LocalDateTime.parse("2020-04-28T19:00:00")));
//        taskEntries2.add(new TaskEntry(4, "send email", LocalDateTime.parse("2020-04-28T20:00:00"), LocalDateTime.parse("2020-04-28T20:00:12")));
//        taskEntries2.add(new TaskEntry(5, "send email", LocalDateTime.parse("2020-04-28T20:45:00"), LocalDateTime.parse("2020-04-28T22:00:00")));
//        taskEntries2.add(new TaskEntry(6, "send email", LocalDateTime.parse("2020-04-28T22:30:00"), LocalDateTime.parse("2020-04-28T23:00:03")));

    }

    private void eatTaskEntries() {
//        taskEntries3.add(new TaskEntry(7, "eat", LocalDateTime.parse("2020-04-28T23:30:00"), LocalDateTime.parse("2020-04-28T23:40:00")));
//        taskEntries3.add(new TaskEntry(8, "eat", LocalDateTime.parse("2020-04-28T23:45:00"), LocalDateTime.parse("2020-04-28T23:48:12")));

    }

    @Override
    public List<Task> getTasksList() {
        return taskList;
    }

    @Override
    public Task createTask(Task task) throws DalException {
        taskList.add(task);
        return task;
    }

    private void createInitialTasks() {
//        taskList.add(new Task(0, "create UI for App", brightTime, taskEntries1));
//        taskList.add(new Task(1, "send email", brightTime, taskEntries2));
//        taskList.add(new Task(2, "eat", brightTime, taskEntries3));

    }

    @Override
    public Map getTasksWithTaskEntries() throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
