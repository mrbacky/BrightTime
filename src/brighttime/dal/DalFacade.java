package brighttime.dal;

import brighttime.be.Client;
import brighttime.be.EventLog;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rados
 */
public interface DalFacade {

    /**
     * Creates a new client in the database.
     *
     * @param client The client to be created.
     * @return The created client.
     * @throws DalException
     */
    Client createClient(Client client) throws DalException;

    /**
     * Gets the clients.
     *
     * @return A list of clients.
     * @throws DalException
     */
    List<Client> getClients() throws DalException;

    /**
     * Creates a new project in the database.
     *
     * @param project The project to be created.
     * @return The created project.
     * @throws DalException
     */
    Project createProject(Project project) throws DalException;

    /**
     * Gets the projects for a selected client.
     *
     * @param client The selected client.
     * @return A list of projects.
     * @throws DalException
     */
    List<Project> getProjects(Client client) throws DalException;

    List<Project> getAllProjects() throws DalException;

    /**
     * Creates a new task in the database.
     *
     * @param task The task to be created.
     * @return The created task.
     * @throws DalException
     */
    TaskConcrete1 createTask(TaskConcrete1 task) throws DalException;

    /**
     * Gets the tasks logged between today and 30 days ago of the user.
     *
     * @param user The user.
     * @return A map with a list of tasks (containing entries) for each day.
     * @throws DalException
     */
    Map<LocalDate, List<TaskConcrete1>> getAllTasksWithEntries(User user) throws DalException;

    /**
     * Creates a task entry in the database.
     *
     * @param taskEntry The task entry to be created.
     * @return The created task entry.
     * @throws DalException
     */
    TaskEntry createTaskEntry(TaskEntry taskEntry) throws DalException;

    TaskEntry updateTaskEntryStartTime(TaskEntry taskEntry) throws DalException;

    TaskEntry updateTaskEntryEndTime(TaskEntry taskEntry) throws DalException;

    List<TaskConcrete2> getAllTasks() throws DalException;

    List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws DalException;

    List<User> getUsers() throws DalException;

    public User authenticateUser(String username, String password) throws DalException;

    public User createUser(User user) throws DalException;

    void logEvent(EventLog log) throws DalException;

}
