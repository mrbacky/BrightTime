package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.dal.DalException;
import brighttime.dal.DalFacade;
import java.io.IOException;
import java.util.List;
import brighttime.bll.util.DurationConverter;
import brighttime.bll.util.EntryDurationCalculator;
import brighttime.bll.util.TaskDurationCalculator;
import brighttime.bll.util.TaskIntervalCalculator;
import brighttime.dal.MockDalManager;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author rado
 */
public class BllManager implements BllFacade {

    private final DurationConverter durationConverter;
    private final DalFacade dalManager;
    private final DalFacade mockDalManager;

    private final EntryDurationCalculator entryDurationCalculator;
    private final TaskDurationCalculator taskDurationCalculator;
    private final TaskIntervalCalculator taskIntervalCalculator;

    public BllManager(DalFacade dalManager) throws IOException {
        this.dalManager = dalManager;
        durationConverter = new DurationConverter();
        entryDurationCalculator = new EntryDurationCalculator();
        taskDurationCalculator = new TaskDurationCalculator();
        taskIntervalCalculator = new TaskIntervalCalculator();
        mockDalManager = new MockDalManager();
    }

    @Override
    public Client createClient(Client client) throws BllException {
        try {
            return dalManager.createClient(client);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public List<Client> getClients() throws BllException {
        try {
            return dalManager.getClients();
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public Project createProject(Project project) throws BllException {
        try {
            return dalManager.createProject(project);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public List<Project> getProjects(Client client) throws BllException {
        try {
            return dalManager.getProjects(client);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public Task createTask(Task task) throws BllException {
        try {
            return dalManager.createTask(task);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public Duration calculateDuration(TaskEntry taskEntry) {
        return entryDurationCalculator.calculateDuration(taskEntry);
    }

    @Override
    public Duration calculateDuration(Task task) {
        return taskDurationCalculator.calculateDuration(task);
    }

    @Override
    public String secToFormat(long sec) {
        return durationConverter.secToFormat(sec);
    }

    @Override
    public long formatToSec(String formatString) {
        return durationConverter.formatToSec(formatString);
    }

    @Override
    public LocalDateTime getStartTime(Task task) {
        return taskIntervalCalculator.getStartTime(task);
    }

    @Override
    public LocalDateTime getEndTime(Task task) {
        return taskIntervalCalculator.getEndTime(task);
    }

    @Override
    public TaskEntry createTaskEntry(TaskEntry taskEntry) throws BllException {
        try {
            return dalManager.createTaskEntry(taskEntry);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public List<TaskEntry> getTaskEntries() throws BllException {
        try {
            return dalManager.getTaskEntries();
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

}
