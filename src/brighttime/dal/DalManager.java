package brighttime.dal;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.dal.dao.concretes.ClientDAO;
import brighttime.dal.dao.concretes.ProjectDAO;
import brighttime.dal.dao.concretes.TaskDAO;
import brighttime.dal.dao.interfaces.IClientDAO;
import brighttime.dal.dao.interfaces.IProjectDAO;
import brighttime.dal.dao.interfaces.ITaskDAO;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rado
 */
public class DalManager implements DalFacade {

    private final IClientDAO clientDAO;
    private final IProjectDAO projectDAO;
    private final ITaskDAO taskDAO;

    public DalManager() throws IOException {
        clientDAO = new ClientDAO();
        projectDAO = new ProjectDAO();
        taskDAO = new TaskDAO();
    }

    @Override
    public Client createClient(Client client) throws DalException {
        try {
            return clientDAO.createClient(client);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Client> getClients() throws DalException {
        try {
            return clientDAO.getClients();
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public Project createProject(Project project) throws DalException {
        try {
            return projectDAO.createProject(project);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Project> getProjects(Client client) throws DalException {
        try {
            return projectDAO.getProjects(client);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public Task createTask(Task task) throws DalException {
        try {
            return taskDAO.createTask(task);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public Map getTasksWithTaskEntries() throws DalException {
        try {
            return taskDAO.getTasksWithTaskEntries();
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Task> getTasksList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
