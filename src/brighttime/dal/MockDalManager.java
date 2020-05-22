package brighttime.dal;

import brighttime.be.Client;
import brighttime.be.EventLog;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
import brighttime.dal.dao.interfaces.ITaskDAO;
import brighttime.dal.dao.mockDAO.MockTaskDAO;
import java.time.LocalDate;
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
    public TaskConcrete1 createTask(TaskConcrete1 task) throws DalException {
        return mockTaskDAO.createTask(task);
    }

    @Override
    public Map<LocalDate, List<TaskConcrete1>> getAllTasksWithEntries(User user) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public TaskEntry updateTaskEntryStartTime(TaskEntry taskEntry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TaskEntry updateTaskEntryEndTime(TaskEntry taskEntry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TaskConcrete2> getAllTasks() throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<User> getUsers() throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User authenticateUser(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User createUser(User user) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void logEvent(EventLog log) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Project> getAllProjects() throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
