package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rados
 */
public interface BllFacade {

    /**
     * Creates a new client in the database.
     *
     * @param client The client to be created.
     * @return The created client.
     * @throws BllException
     */
    Client createClient(Client client) throws BllException;

    /**
     * Gets the clients.
     *
     * @return A list of clients.
     * @throws BllException
     */
    List<Client> getClients() throws BllException;

    /**
     * Creates a new project in the database.
     *
     * @param project The project to be created.
     * @return The created project.
     * @throws BllException
     */
    Project createProject(Project project) throws BllException;

    /**
     * Gets the projects for a selected client.
     *
     * @param client The selected client.
     * @return A list of projects.
     * @throws BllException
     */
    List<Project> getProjects(Client client) throws BllException;

    /**
     * Creates a new task in the database.
     *
     * @param task The task to be created.
     * @return The created task.
     * @throws BllException
     */
    TaskConcrete1 createTask(TaskConcrete1 task) throws BllException;

    /**
     * Gets the tasks logged between today and 30 days ago.
     *
     * @return A map with a list of tasks (containing entries) for each day.
     * @throws BllException
     */
    Map<LocalDate, List<TaskConcrete1>> Tasks() throws BllException;

    Duration calculateDuration(TaskEntry taskEntry);

    Duration calculateTaskDuration(List<TaskEntry> entryList);

    String secToFormat(long sec);

    long formatToSec(String formatString);

    LocalDateTime getStartTime(List<TaskEntry> taskEntries);

    LocalDateTime getEndTime(List<TaskEntry> taskEntries);

    /**
     * Creates a task entry in the database.
     *
     * @param taskEntry The task entry to be created.
     * @return The created task entry.
     * @throws BllException
     */
    TaskEntry createTaskEntry(TaskEntry taskEntry) throws BllException;

    TaskEntry updateTaskEntryStartTime(TaskEntry taskEntry) throws BllException;

    TaskEntry updateTaskEntryEndTime(TaskEntry taskEntry) throws BllException;

    List<TaskConcrete2> getAllTasks() throws BllException;

    List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws BllException;

    String formatDuration(int durationSeconds);

    String formatCost(double cost);

}
