package brighttime.dal;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.dal.dao.concretes.ClientDAO;
import brighttime.dal.dao.concretes.ProjectDAO;
import brighttime.dal.dao.concretes.TaskDAO;
import brighttime.dal.dao.concretes.TaskEntryDAO;
import brighttime.dal.dao.interfaces.IClientDAO;
import brighttime.dal.dao.interfaces.IProjectDAO;
import brighttime.dal.dao.interfaces.ITaskDAO;
import brighttime.dal.dao.interfaces.ITaskEntryDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rado
 */
public class DalManager implements DalFacade {

    private final IClientDAO clientDAO;
    private final IProjectDAO projectDAO;
    private final ITaskDAO taskDAO;
    private final ITaskEntryDAO taskEntryDAO;

    public DalManager() throws IOException {
        clientDAO = new ClientDAO();
        projectDAO = new ProjectDAO();
        taskDAO = new TaskDAO();
        taskEntryDAO = new TaskEntryDAO();
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
    public TaskEntry createTaskEntry(TaskEntry taskEntry) throws DalException {
        try {
            return taskEntryDAO.createTaskEntry(taskEntry);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public Map<LocalDate, List<Task>> Tasks() throws DalException {
        try {
            return taskDAO.Tasks();
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public TaskEntry updateTaskEntryStartTime(TaskEntry taskEntry) {
        return taskEntryDAO.updateTaskEntryStartTime(taskEntry);

    }

    @Override
    public TaskEntry updateTaskEntryEndTime(TaskEntry taskEntry) {
        return taskEntryDAO.updateTaskEntryEndTime(taskEntry);
    }

}
