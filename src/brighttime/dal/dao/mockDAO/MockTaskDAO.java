package brighttime.dal.dao.mockDAO;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.dal.dao.interfaces.ITaskDAO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Duration;

/**
 *
 * @author rado
 */
public class MockTaskDAO implements ITaskDAO {

    private List<TaskEntry> taskEntries1 = new ArrayList<>();
    private List<TaskEntry> taskEntries2 = new ArrayList<>();
    private List<TaskEntry> taskEntries3 = new ArrayList<>();

    private Project brightTime;
    private Client grumsen;

    public MockTaskDAO() {
        createClient();
        createProject();
        createUITaskEntries();
        sendEmailTaskEntries();
        eatTaskEntries();

    }

    private void createClient() {
        grumsen = new Client(0, "Grumsen Development");
    }

    private void createProject() {
        brightTime = new Project(0, "BrightTime", grumsen);
    }

    private void createUITaskEntries() {

        taskEntries1.add(new TaskEntry(0, "create UI for App", LocalDateTime.parse("2020-04-28T08:00:00"), LocalDateTime.parse("2020-04-28T09:00:00")));
        taskEntries1.add(new TaskEntry(1, "create UI for App", LocalDateTime.parse("2020-04-28T10:00:00"), LocalDateTime.parse("2020-04-28T11:20:00")));
        taskEntries1.add(new TaskEntry(2, "create UI for App", LocalDateTime.parse("2020-04-28T15:00:00"), LocalDateTime.parse("2020-04-28T18:15:00")));

    }

    private void sendEmailTaskEntries() {
        taskEntries2.add(new TaskEntry(3, "send email", LocalDateTime.parse("2020-04-28T10:00:00"), LocalDateTime.parse("2020-04-28T11:00:00")));
        taskEntries2.add(new TaskEntry(4, "send email", LocalDateTime.parse("2020-04-28T17:00:00"), LocalDateTime.parse("2020-04-28T17:00:12")));
        taskEntries2.add(new TaskEntry(5, "send email", LocalDateTime.parse("2020-04-28T20:45:00"), LocalDateTime.parse("2020-04-28T22:00:00")));
        taskEntries2.add(new TaskEntry(6, "send email", LocalDateTime.parse("2020-04-28T22:30:00"), LocalDateTime.parse("2020-04-28T23:00:03")));

    }

    private void eatTaskEntries() {
        taskEntries3.add(new TaskEntry(7, "eat", LocalDateTime.parse("2020-04-28T10:00:00"), LocalDateTime.parse("2020-04-28T11:00:00")));
        taskEntries3.add(new TaskEntry(8, "eat", LocalDateTime.parse("2020-04-28T17:00:00"), LocalDateTime.parse("2020-04-28T17:00:12")));
        taskEntries3.add(new TaskEntry(9, "eat", LocalDateTime.parse("2020-04-28T20:45:00"), LocalDateTime.parse("2020-04-28T22:00:00")));
        taskEntries3.add(new TaskEntry(10, "eat", LocalDateTime.parse("2020-04-28T22:30:00"), LocalDateTime.parse("2020-04-28T23:00:03")));
        taskEntries3.add(new TaskEntry(11, "eat", LocalDateTime.parse("2020-04-28T22:30:00"), LocalDateTime.parse("2020-04-28T23:00:03")));
        taskEntries3.add(new TaskEntry(12, "eat", LocalDateTime.parse("2020-04-28T22:30:00"), LocalDateTime.parse("2020-04-28T23:00:03")));

    }

    @Override
    public List<Task> loadTasks() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(0, "create UI for App", brightTime, taskEntries1));
        taskList.add(new Task(1, "send email", brightTime, taskEntries2));
        taskList.add(new Task(2, "eat", brightTime, taskEntries3));

        return taskList;
    }

}
