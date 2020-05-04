package brighttime.dal;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.dal.dao.interfaces.ITaskDAO;
import brighttime.dal.dao.mockDAO.MockTaskDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rado
 */
public class MockDalManager implements DalFacade {

    private ITaskDAO MockTaskDAO;

    public MockDalManager() {
        MockTaskDAO = new MockTaskDAO();

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
        return MockTaskDAO.createTask(task);
    }

    @Override
    public Map getTasksWithTaskEntries() throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Task> getTasksList() {
        return MockTaskDAO.getTasksList();
    }

    @Override
    public Client createClient(Client client) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Project createProject(Project project) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
