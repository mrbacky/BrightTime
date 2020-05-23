package brighttime.dal;

import brighttime.be.Client;
import brighttime.be.EventLog;
import brighttime.be.Filter;
import brighttime.be.Project;
import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
import brighttime.dal.dao.concretes.ClientDAO;
import brighttime.dal.dao.concretes.EventLogDAO;
import brighttime.dal.dao.concretes.ProjectDAO;
import brighttime.dal.dao.concretes.TaskDAO;
import brighttime.dal.dao.concretes.TaskEntryDAO;
import brighttime.dal.dao.concretes.UserDAO;
import brighttime.dal.dao.interfaces.IClientDAO;
import brighttime.dal.dao.interfaces.IEventLogDAO;
import brighttime.dal.dao.interfaces.IProjectDAO;
import brighttime.dal.dao.interfaces.ITaskDAO;
import brighttime.dal.dao.interfaces.ITaskEntryDAO;
import brighttime.dal.dao.interfaces.IUserDAO;
import java.io.IOException;
import java.time.LocalDate;
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
    private final ITaskEntryDAO taskEntryDAO;
    private IUserDAO userDAO;
    private IEventLogDAO eventLog;

    public DalManager() throws IOException {
        clientDAO = new ClientDAO();
        projectDAO = new ProjectDAO();
        taskDAO = new TaskDAO();
        taskEntryDAO = new TaskEntryDAO();
        userDAO = new UserDAO();
        eventLog = new EventLogDAO();
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
    public Client updateClient(Client client) throws DalException {
        try {
            return clientDAO.updateClient(client);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public Client deleteClient(Client client) throws DalException {
        try {
            return clientDAO.deleteClient(client);
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
    public List<Project> getAllProjects() throws DalException {
        try {
            return projectDAO.getAllProjects();
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public void updateProject(Project project) throws DalException {
        try {
            projectDAO.updateProject(project);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public void deleteProject(Project project) throws DalException {
        try {
            projectDAO.deleteProject(project);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public TaskConcrete1 createTask(TaskConcrete1 task) throws DalException {
        try {
            return taskDAO.createTask(task);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public TaskEntry createTaskEntry(TaskEntry taskEntry) throws DalException {
        try {
            return taskEntryDAO.createTaskEntry(taskEntry);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public Map<LocalDate, List<TaskConcrete1>> getAllTasksWithEntries(User user) throws DalException {
        try {
            return taskDAO.getAllTasksWithEntries(user);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public TaskEntry updateTaskEntryStartTime(TaskEntry taskEntry) throws DalException {
        try {
            return taskEntryDAO.updateTaskEntryStartTime(taskEntry);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }

    }

    @Override
    public TaskEntry updateTaskEntryEndTime(TaskEntry taskEntry) throws DalException {
        try {
            return taskEntryDAO.updateTaskEntryEndTime(taskEntry);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<TaskConcrete2> getAllTasks() throws DalException {
        try {
            return taskDAO.getAllTasks();
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws DalException {
        try {
            return taskDAO.getAllTasksFiltered(filter);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<User> getUsers() throws DalException {
        try {
            return userDAO.getUsers();
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public User authenticateUser(String username, String password) throws DalException {
        try {
            return userDAO.authenticateUser(username, password);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public boolean checkUsernameAvailability(String username) throws DalException {
        try {
            return userDAO.checkUsernameAvailability(username);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public User createUser(User user) throws DalException {
        return userDAO.createUser(user);

    }

    @Override
    public void logEvent(EventLog log) throws DalException {
        try {
            eventLog.logEvent(log);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public User updateUserDetails(User updatedUser) throws DalException {
        return userDAO.updateUserDetails(updatedUser);
    }

    @Override
    public User deleteUser(User selectedUser) throws DalException {
        return userDAO.deleteUser(selectedUser);
    }

}
