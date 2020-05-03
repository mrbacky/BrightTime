package brighttime.gui.model.concretes;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.bll.BllException;
import brighttime.bll.BllFacade;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author rado
 */
public class MainModel implements IMainModel {

    private BllFacade bllManager;
    private final ObservableList<Task> taskList = FXCollections.observableArrayList();
    private final ObservableList<Client> clientList = FXCollections.observableArrayList();
    private final ObservableList<Project> projectList = FXCollections.observableArrayList();

    public MainModel(BllFacade bllManager) {
        this.bllManager = bllManager;
    }

    @Override
    public void loadTasks() {
        List<Task> allTasks = bllManager.getTasks();
        taskList.clear();
        taskList.addAll(allTasks);
    }

    @Override
    public ObservableList<Task> getTasks() {
        return taskList;
    }

    @Override
    public Client addClient(Client client) throws ModelException {
        try {
            return bllManager.createClient(client);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadClients() throws ModelException {
        try {
            List<Client> allClients = bllManager.getClients();
            clientList.clear();
            clientList.addAll(allClients);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public ObservableList<Client> getClientList() {
        return clientList;
    }

    @Override
    public Project addProject(Project project) throws ModelException {
        try {
            return bllManager.createProject(project);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadProjects(Client client) throws ModelException {
        try {
            List<Project> allProjects = bllManager.getProjects(client);
            projectList.clear();
            projectList.addAll(allProjects);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public ObservableList<Project> getProjectList() {
        return projectList;
    }

    @Override
    public void addTask(Task task) {
        taskList.add(task);
        try {
            bllManager.createTask(task);
        } catch (BllException ex) {
            Logger.getLogger(MainModel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
