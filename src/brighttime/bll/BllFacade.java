package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import java.util.List;

/**
 *
 * @author rados
 */
public interface BllFacade {

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

    List<Task> loadTasks();

    /**
     * Creates a new task in the database.
     *
     * @param task The task to be created.
     * @return The created task.
     * @throws BllException
     */
    Task createTask(Task task) throws BllException;

    String convertDuration(int duration);

    int convertDuration(String duration);

}
