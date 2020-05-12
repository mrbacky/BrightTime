package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Filter;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

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
    public Map<LocalDate, List<Task>> Tasks() throws BllException {
        try {
            return dalManager.Tasks();
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
    public List<Task> getAllTasks() throws BllException {
        try {
            List<Task> allTasks = dalManager.getAllTasks();
            Map<Integer, Integer> rateMap = dalManager.getRate();
            for (Task allTask : allTasks) {
                if (allTask.getBillability() == Task.Billability.BILLABLE) {
                    int totalCost = calculateTotalCost(allTask.getTotalDuration(), rateMap.get(allTask.getProjectId()));
                    allTask.setTotalCost(totalCost);
                } else {
                    allTask.setTotalCost(0);
                }
            }
            return allTasks;
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }

    //TODO: Make a utility class with this method. Where to round to closest quarter?
    private int calculateTotalCost(int durationSeconds, int rate) {
        int d = (durationSeconds * rate) / (60 * 60);
        double durationHours = durationSeconds / (60 * 60);
        return d;
    }

    @Override
    public List<Task> getAllTasksFiltered(Filter filter) throws BllException {
        try {
            List<Task> filtered = dalManager.getAllTasksFiltered(filter);
            for (Task task : filtered) {
                if (task.getBillability() == Task.Billability.BILLABLE) {
                    int totalCost = calculateTotalCost(task.getTotalDuration(), task.getRate());
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

}
