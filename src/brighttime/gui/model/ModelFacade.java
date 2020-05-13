package brighttime.gui.model;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.TaskBase;
import javafx.collections.ObservableList;

/**
 *
 * @author rados
 */
public interface ModelFacade {

    /**
     * Adds a new client.
     *
     * @param client The new client.
     * @return The created client from the database.
     * @throws ModelException
     */
    Client addClient(Client client) throws ModelException;

    /**
     * Gets the clients and adds them to an ObservableList.
     *
     * @throws ModelException
     */
    void loadClients() throws ModelException;

    /**
     * Gets the ObservableList containing clients.
     *
     * @return The ObservableList of clients.
     */
    ObservableList<Client> getClientList();

    /**
     * Adds a new project.
     *
     * @param project The new project.
     * @return The created project from the database.
     * @throws ModelException
     */
    Project addProject(Project project) throws ModelException;

    /**
     * Gets the projects for a selected client and adds them to an
     * ObservableList.
     *
     * @param client The selected client.
     * @throws ModelException
     */
    void loadProjects(Client client) throws ModelException;

    /**
     * Gets the ObservableList containing projects.
     *
     * @return The ObservableList of projects.
     */
    ObservableList<Project> getProjectList();

    /**
     * Adds a new task.
     *
     * @param task The new task.
     * @return The created task from the database.
     * @throws ModelException
     */
    TaskBase addTask(TaskBase task) throws ModelException;

    void loadTasks();

    ObservableList<TaskBase> getTasks();

}
