package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
    Task createTask(Task task) throws BllException;

    List<TaskEntry> getTaskEntries() throws BllException;

    Duration calculateDuration(TaskEntry taskEntry);

    Duration calculateDuration(Task task);

    String secToFormat(long sec);

    long formatToSec(String formatString);

    LocalDateTime getStartTime(Task task);

    LocalDateTime getEndTime(Task task);

    /**
     * Creates a task entry in the database.
     *
     * @param taskEntry The task entry to be created.
     * @return The created task entry.
     * @throws BllException
     */
    TaskEntry createTaskEntry(TaskEntry taskEntry) throws BllException;
}
