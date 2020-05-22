package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.EventLog;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskBase;
import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
import brighttime.bll.util.CostCalculator;
import brighttime.bll.util.CostFormatter;
import brighttime.dal.DalException;
import brighttime.dal.DalFacade;
import java.io.IOException;
import java.util.List;
import brighttime.bll.util.DurationConverter;
import brighttime.bll.util.DurationFormatter;
import brighttime.bll.util.EntryDurationCalculator;
import brighttime.bll.util.TaskDurationCalculator;
import brighttime.bll.util.TaskIntervalCalculator;
import brighttime.dal.MockDalManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final CostCalculator costCalculator;
    private final CostFormatter costFormatter;
    private final DurationFormatter durationFormatter;

    public BllManager(DalFacade dalManager) throws IOException {
        this.dalManager = dalManager;
        durationConverter = new DurationConverter();
        entryDurationCalculator = new EntryDurationCalculator();
        taskDurationCalculator = new TaskDurationCalculator();
        taskIntervalCalculator = new TaskIntervalCalculator();
        costCalculator = new CostCalculator();
        costFormatter = new CostFormatter();
        durationFormatter = new DurationFormatter();
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
    public TaskConcrete1 createTask(TaskConcrete1 task) throws BllException {
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
    public Duration calculateTaskDuration(List<TaskEntry> entryList) {
        return taskDurationCalculator.calculateTaskDuration(entryList);
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
    public LocalDateTime getStartTime(List<TaskEntry> taskEntries) {
        return taskIntervalCalculator.getStartTime(taskEntries);
    }

    @Override
    public LocalDateTime getEndTime(List<TaskEntry> taskEntries) {
        return taskIntervalCalculator.getEndTime(taskEntries);
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
    public Map<LocalDate, List<TaskConcrete1>> getAllTasksWithEntries(User user) throws BllException {
        try {
            return dalManager.getAllTasksWithEntries(user);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public TaskEntry updateTaskEntryStartTime(TaskEntry taskEntry) throws BllException {
        try {
            return dalManager.updateTaskEntryStartTime(taskEntry);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public TaskEntry updateTaskEntryEndTime(TaskEntry taskEntry) throws BllException {
        try {
            return dalManager.updateTaskEntryEndTime(taskEntry);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }

    }

    @Override
    public List<TaskConcrete2> getAllTasks() throws BllException {
        try {
            List<TaskConcrete2> allTasks = dalManager.getAllTasks();
            for (TaskConcrete2 task : allTasks) {
                if (task.getBillability() == TaskBase.Billability.BILLABLE) {
                    double totalCost = costCalculator.calculateTotalCost(task.getTotalDurationSeconds(), task.getRate());
                    task.setTotalCost(totalCost);
                } else {
                    task.setTotalCost(0);
                }
            }
            return allTasks;
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws BllException {
        try {
            List<TaskConcrete2> filtered = dalManager.getAllTasksFiltered(filter);
            for (TaskConcrete2 task : filtered) {
                if (task.getBillability() == TaskBase.Billability.BILLABLE) {
                    double totalCost = costCalculator.calculateTotalCost(task.getTotalDurationSeconds(), task.getRate());
                    task.setTotalCost(totalCost);
                } else {
                    task.setTotalCost(0);
                }
            }
            return filtered;
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public String formatDuration(int durationSeconds) {
        return durationFormatter.formatDuration(durationSeconds);
    }

    @Override
    public String formatCost(double cost) {
        return costFormatter.formatCost(cost);
    }

    @Override
    public List<User> getUsers() throws BllException {
        try {
            return dalManager.getUsers();
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public User authenticateUser(String username, String password) throws BllException {
        try {
            return dalManager.authenticateUser(username, password);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    /**
     *
     * @param user
     * @return
     * @throws BllException
     */
    @Override
    public User createUser(User user) throws BllException {
        try {
            return dalManager.createUser(user);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public void logEvent(EventLog log) throws BllException {
        try {
            dalManager.logEvent(log);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public User updateUserDetails(User updatedUser) throws BllException {
        try {
            return dalManager.updateUserDetails(updatedUser);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    @Override
    public User deleteUser(User selectedUser) throws BllException {
        try {
            return dalManager.deleteUser(selectedUser);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());

        }
    }

}
