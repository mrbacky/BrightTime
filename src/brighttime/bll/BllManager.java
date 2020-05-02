package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.dal.DalException;
import brighttime.dal.DalFacade;
import brighttime.dal.DalManager;
import java.io.IOException;
import java.util.List;
import brighttime.bll.util.DurationConverter;
import brighttime.bll.util.EntryDurationCalculator;
import brighttime.bll.util.TaskDurationCalculator;
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

    public BllManager() throws IOException {
        dalManager = new DalManager();
        durationConverter = new DurationConverter();
        mockDalManager = new MockDalManager();
        entryDurationCalculator = new EntryDurationCalculator();
        taskDurationCalculator = new TaskDurationCalculator();
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
    public List<Task> getTasks() {
        return mockDalManager.getTasks();
    }

    @Override
    public Duration calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        return entryDurationCalculator.calculateDuration(startTime, endTime);
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

}
