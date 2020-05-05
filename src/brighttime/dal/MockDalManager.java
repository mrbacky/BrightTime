package brighttime.dal;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.dal.dao.interfaces.ITaskDAO;
import brighttime.dal.dao.mockDAO.MockTaskDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rado
 */
public class MockDalManager implements DalFacade {

    private ITaskDAO mockTaskDAO;

    public MockDalManager() {
        mockTaskDAO = new MockTaskDAO();

    }

    @Override
    public List<Client> getClients() throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Project> getProjects(Client client) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Task createTask(Task task) throws DalException {
        return mockTaskDAO.createTask(task);
    }

    @Override
    public Map getTasksWithTaskEntries() throws DalException {
        return mockTaskDAO.getTasksWithTaskEntries();
    }

    @Override
    public List<Task> getTasksList() {
        return mockTaskDAO.getTasksList();
    }

    @Override
    public Client createClient(Client client) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Project createProject(Project project) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TaskEntry createTaskEntry(TaskEntry taskEntry) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
