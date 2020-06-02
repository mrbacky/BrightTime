package brighttime.dal;

import brighttime.be.Client;
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
     * Updates a client in the database.
     *
     * @param client The client to be updated.
     * @return The updated client.
     * @throws DalException
     */
    Client updateClient(Client client) throws DalException;

    /**
     * Deletes a client in the database.
     *
     * @param client The client to be deleted.
     * @return The deleted client.
     * @throws DalException
     */
    Client deleteClient(Client client) throws DalException;

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
    List<Project> getProjectsForAClient(Client client) throws DalException;

    /**
     * Gets all the projects from the database.
     *
     * @return A list of all projects.
     * @throws DalException
     */
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
     * Gets the tasks logged by the user between a start date and an end date.
     *
     * @param filter A filter with the parameters user, start date and end date.
     * @return A map with a list of tasks (containing entries) for each date.
     * @throws DalException
     */
    Map<LocalDate, List<TaskConcrete1>> getAllTasksWithEntries(Filter filter) throws DalException;

    /**
     * Creates a task entry in the database.
     *
     * @param taskEntry The task entry to be created.
     * @return The created task entry.
     * @throws DalException
     */
    TaskEntry createTaskEntry(TaskEntry taskEntry) throws DalException;

    /**
     * Updates the start time of a time entry.
     *
     * @param taskEntry The task entry to be updated.
     * @return The updated task entry.
     * @throws DalException
     */
    TaskEntry updateTaskEntryStartTime(TaskEntry taskEntry) throws DalException;

    /**
     * Updates the end time of a time entry.
     *
     * @param taskEntry The task entry to be updated.
     * @return The updated task entry.
     * @throws DalException
     */
    TaskEntry updateTaskEntryEndTime(TaskEntry taskEntry) throws DalException;

    /**
     * Gets all the tasks for the Overview.
     *
     * @return A list of tasks.
     * @throws DalException
     */
    List<TaskConcrete2> getAllTasks() throws DalException;

    /**
     * Gets the tasks which satisfies the filter condition for the Overview.
     *
     * @param filter The filter.
     * @return A list of filtered tasks.
     * @throws DalException
     */
    List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws DalException;

    /**
     * Creates the new user in the database.
     *
     * @param user The user to create.
     * @return The created user.
     * @throws DalException
     */
    User createUser(User user) throws DalException;

    /**
     * Gets all the active users from the database.
     *
     * @return A list of active users.
     * @throws DalException
     */
    List<User> getUsers() throws DalException;

    /**
     * Authenticates the user.
     *
     * @param username The username.
     * @param password The password.
     * @return The authenticated user.
     * @throws DalException
     */
    User authenticateUser(String username, String password) throws DalException;

    /**
     * Updates the user.
     *
     * @param user The user to be updated.
     * @return The updated user.
     * @throws DalException
     */
    User updateUserDetails(User user) throws DalException;

    /**
     * Deletes the user. Users with tasks are permanently deleted, while users
     * with tasks are marked as inactive and removed from the application.
     *
     * @param user The user to be deleted.
     * @return The deleted user.
     * @throws DalException
     */
    User deleteUser(User user) throws DalException;

}
