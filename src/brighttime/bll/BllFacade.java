package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
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
     * Updates a client in the database.
     *
     * @param client The client to be updated.
     * @throws BllException
     */
    void updateClient(Client client) throws BllException;

    /**
     * Deletes a client in the database.
     *
     * @param client The client to be deleted.
     * @return The deleted client.
     * @throws BllException
     */
    Client deleteClient(Client client) throws BllException;

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
     * @return A list of projects for the client.
     * @throws BllException
     */
    List<Project> getProjectsForAClient(Client client) throws BllException;

    /**
     * Gets all the projects from the database.
     *
     * @return A list of all projects.
     * @throws BllException
     */
    List<Project> getAllProjects() throws BllException;

    /**
     * Creates a new task in the database.
     *
     * @param task The task to be created.
     * @return The created task.
     * @throws BllException
     */
    TaskConcrete1 createTask(TaskConcrete1 task) throws BllException;

    /**
     * Creates a task entry in the database.
     *
     * @param taskEntry The task entry to be created.
     * @return The created task entry.
     * @throws BllException
     */
    TaskEntry createTaskEntry(TaskEntry taskEntry) throws BllException;

    /**
     * Gets the tasks logged by the user between a start date and an end date.
     *
     * @param filter A filter with the parameters user, start date and end date.
     * @return A map with a list of tasks (containing entries) for each date.
     * @throws BllException
     */
    Map<LocalDate, List<TaskConcrete1>> getAllTasksWithEntries(Filter filter) throws BllException;

    Duration calculateDuration(TaskEntry taskEntry);

    Duration calculateTaskDuration(List<TaskEntry> entryList);

    String secToFormat(int sec);

    long formatToSec(String formatString);

    LocalDateTime getStartTime(List<TaskEntry> taskEntries);

    LocalDateTime getEndTime(List<TaskEntry> taskEntries);

    /**
     * Updates the start time of a time entry.
     *
     * @param taskEntry The task entry to be updated.
     * @throws BllException
     */
    void updateTaskEntryStartTime(TaskEntry taskEntry) throws BllException;

    /**
     * Updates the end time of a time entry.
     *
     * @param taskEntry The task entry to be updated.
     * @throws BllException
     */
    void updateTaskEntryEndTime(TaskEntry taskEntry) throws BllException;

    /**
     * Gets all the tasks for the Overview.
     *
     * @return A list of tasks.
     * @throws BllException
     */
    List<TaskConcrete2> getAllTasks() throws BllException;

    /**
     * Gets the tasks which satisfies the filter condition for the Overview.
     *
     * @param filter The filter.
     * @return A list of filtered tasks.
     * @throws BllException
     */
    List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws BllException;

    /**
     * Formats the duration for the view.
     *
     * @param durationSeconds The total duration in seconds.
     * @return The duration in the desired format as a String.
     */
    String formatDuration(int durationSeconds);

    /**
     * Formats the cost for the view.
     *
     * @param cost The total cost.
     * @return The cost in the desired format as a String.
     */
    String formatCost(double cost);

    /**
     * Creates the new user in the database.
     *
     * @param user The user to create.
     * @return The created user.
     * @throws BllException
     */
    User createUser(User user) throws BllException;

    /**
     * Gets all the active users from the database.
     *
     * @return A list of active users.
     * @throws BllException
     */
    List<User> getUsers() throws BllException;

    /**
     * Authenticates the user.
     *
     * @param username The username.
     * @param password The password.
     * @return The authenticated user.
     * @throws BllException
     */
    User authenticateUser(String username, String password) throws BllException;

    /**
     * Updates the user.
     *
     * @param user The user to be updated.
     * @throws BllException
     */
    void updateUserDetails(User user) throws BllException;

    /**
     * Deletes the user.
     *
     * @param user The user to be deleted.
     * @return The deleted user.
     * @throws BllException
     */
    User deleteUser(User user) throws BllException;

}
