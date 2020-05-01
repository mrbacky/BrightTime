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

    List<Task> getTasks();

    String convertDuration(int duration);

    int convertDuration(String duration);

}
