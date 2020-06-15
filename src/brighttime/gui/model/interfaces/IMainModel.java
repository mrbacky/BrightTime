package brighttime.gui.model.interfaces;

import brighttime.be.Client;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
import brighttime.gui.model.ModelException;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 *
 * @author rados
 */
public interface IMainModel {

    void setUser(User user);

    User getUser();

    /**
     * Adds a new client.
     *
     * @param client The new client.
     * @throws ModelException
     */
    void addClient(Client client) throws ModelException;

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

    void updateClient(Client client) throws ModelException;

    void deleteClient(Client client) throws ModelException;

    /**
     * Adds a new project.
     *
     * @param project The new project.
     * @throws ModelException
     */
    void addProject(Project project) throws ModelException;

    /**
     * Gets the projects for a selected client and adds them to an
     * ObservableList.
     *
     * @param client The selected client.
     * @throws ModelException
     */
    void loadProjects(Client client) throws ModelException;

    /**
     * Gets all projects and adds them to an ObservableList.
     *
     * @throws ModelException
     */
    void loadAllProjects() throws ModelException;

    /**
     * Gets the ObservableList containing projects.
     *
     * @return The ObservableList of projects.
     */
    ObservableList<Project> getProjectList();

    void addTask(TaskConcrete1 task) throws ModelException;

    void loadTasks(Filter filter) throws ModelException;

    ObservableMap<LocalDate, List<TaskConcrete1>> getTaskMap();

    void addTaskMapListener(MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener);

    void loadOverviewTasks() throws ModelException;

    void loadOverviewTasksFiltered(Filter filter) throws ModelException;

    ObservableList<TaskConcrete2> getOverviewTaskList();

    void createUser(User user) throws ModelException;

    void loadUsers() throws ModelException;

    ObservableList<User> getUserList();

    void updateUserDetails(User user) throws ModelException;

    void deleteUser(User user) throws ModelException;

}
