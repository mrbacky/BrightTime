package brighttime.gui.model;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.gui.model.concretes.ClientModel;
import brighttime.gui.model.concretes.ProjectModel;
import brighttime.gui.model.concretes.TaskModel;
import brighttime.gui.model.interfaces.IClientModel;
import brighttime.gui.model.interfaces.IProjectModel;
import brighttime.gui.model.interfaces.ITaskModel;
import java.io.IOException;
import javafx.collections.ObservableList;

/**
 *
 * @author rado
 */
public class ModelManager implements ModelFacade {

    private final IClientModel clientModel;
    private final IProjectModel projectModel;
    private final ITaskModel taskModel;

    public ModelManager() throws IOException {
        clientModel = new ClientModel();
        projectModel = new ProjectModel();
        taskModel = new TaskModel();
    }

    @Override
    public Client addClient(Client client) throws ModelException {
        try {
            return clientModel.addClient(client);
        } catch (ModelException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadClients() throws ModelException {
        try {
            clientModel.loadClients();
        } catch (ModelException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public ObservableList<Client> getClientList() {
        return clientModel.getClientList();
    }

    @Override
    public void loadProjects(Client client) throws ModelException {
        try {
            projectModel.loadProjects(client);
        } catch (ModelException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public ObservableList<Project> getProjectList() {
        return projectModel.getProjectList();
    }

    @Override
    public Task addTask(Task task) throws ModelException {
        try {
            return taskModel.addTask(task);
        } catch (ModelException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadTasks() {
        taskModel.loadTasks();
    }

    @Override
    public ObservableList<Task> getTasks() {
        return taskModel.getTasks();
    }

}
