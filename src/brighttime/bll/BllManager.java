package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.dal.DalException;
import brighttime.dal.DalFacade;
import brighttime.dal.DalManager;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author rado
 */
public class BllManager implements BllFacade {
    
    private final DalFacade dalManager;
    
    public BllManager() throws IOException {
        dalManager = new DalManager();
    }
    
    @Override
    public List<Task> loadTasks() {
        return null;
    }
    
    @Override
    public List<Client> getClients() throws BllException {
        try {
            return dalManager.getClients();
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }
    
    @Override
    public List<Project> getProjects(Client client) throws BllException {
        try {
            return dalManager.getProjects(client);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }
    
    @Override
    public Task createTask(Task task) throws BllException {
        try {
            return dalManager.createTask(task);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }
    
    @Override
    public TaskEntry createTaskEntry(TaskEntry taskEntry) throws BllException {
        try {
            return dalManager.createTaskEntry(taskEntry);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
    }
    
}
