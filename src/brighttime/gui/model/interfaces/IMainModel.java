package brighttime.gui.model.interfaces;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.gui.model.ModelException;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 *
 * @author rados
 */
public interface IMainModel {

    void loadTasks();

    ObservableMap<Integer, Task> getTasks();

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

    void addTask(Task task);

    void loadTaskEntries() throws ModelException;

    ObservableList<TaskEntry> getTaskEntryList();

}
