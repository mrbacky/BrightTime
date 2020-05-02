package brighttime.gui.model;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.gui.model.concretes.ClientModel;
import brighttime.gui.model.concretes.ProjectModel;
import brighttime.gui.model.concretes.TaskModel;
import brighttime.gui.model.interfaces.IClientModel;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.model.interfaces.IProjectModel;
import brighttime.gui.model.interfaces.ITaskModel;
import java.io.IOException;
import javafx.collections.ObservableList;

/**
 *
 * @author rado
 */
public class ModelManager implements ModelFacade {

    private IClientModel clientModel;
    private IProjectModel projectModel;
    private ITaskModel taskModel;
    private IMainModel mainModel;

    public ModelManager() throws IOException {
        mainModel = ModelCreator.getInstance().createMainModel();
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
    public Project addProject(Project project) throws ModelException {
        try {
            return projectModel.addProject(project);
        } catch (ModelException ex) {
            throw new ModelException(ex.getMessage());
        }
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

//    @Override
//    public Task addTask(Task task) throws ModelException {
//        try {
//            return taskModel.addTask(task);
//        } catch (ModelException ex) {
//            throw new ModelException(ex.getMessage());
//        }
//    }
//
//    @Override
//    public void loadTasks() {
//        taskModel.loadTasks();
//    }
//
//    @Override
//    public ObservableList<Task> getTasks() {
//        return taskModel.getTasks();
//    }

    @Override
    public Task addTask(Task task) throws ModelException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadTasks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObservableList<Task> getTasks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
