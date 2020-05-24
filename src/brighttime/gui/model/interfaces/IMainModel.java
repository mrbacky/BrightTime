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

    void loadAllProjects() throws ModelException;

    /**
     * Gets the ObservableList containing projects.
     *
     * @return The ObservableList of projects.
     */
    ObservableList<Project> getProjectList();

    void addTask(TaskConcrete1 task) throws ModelException;

    ObservableMap<LocalDate, List<TaskConcrete1>> getTasks();

    void loadTasks(User user) throws ModelException;

    ObservableList<TaskConcrete2> getTaskList();

    void getAllTasks() throws ModelException;

    void getAllTasksFiltered(Filter filter) throws ModelException;

    void loadUsers() throws ModelException;

    ObservableList<User> getUserList();

    void setUser(User user);

    User getUser();

    void addTaskMapListener(MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener);

    int checkUsernameAvailability(String username) throws ModelException;

    void createUser(User user) throws ModelException;

    User updateUserDetails(User updatedUser) throws ModelException;

    void deleteUser(User selectedUser) throws ModelException;

    void updateClient(Client selectedClient) throws ModelException;

    void deleteClient(Client c) throws ModelException;

}
