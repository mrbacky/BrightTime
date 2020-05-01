package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import brighttime.dal.DalException;
import brighttime.dal.DalFacade;
import brighttime.dal.DalManager;
import java.io.IOException;
import java.util.List;
import brighttime.bll.util.DurationConverter;
import brighttime.dal.MockDalManager;

/**
 *
 * @author rado
 */
public class BllManager implements BllFacade {

    private final DurationConverter durationConverter;
    private final DalFacade dalManager;
    private final DalFacade mockDalManager;

    public BllManager() throws IOException {
        dalManager = new DalManager();
        durationConverter = new DurationConverter();
        mockDalManager = new MockDalManager();
    }

    @Override
    public Client createClient(Client client) throws BllException {
        try {
            return dalManager.createClient(client);
        } catch (DalException ex) {
            throw new BllException(ex.getMessage());
        }
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
    public List<Task> getTasks() {
        return mockDalManager.getTasks();
    }

    @Override
    public String convertDuration(int duration) {
        return durationConverter.sec_To_Format(duration);
    }

    @Override
    public int convertDuration(String duration) {
        return durationConverter.format_To_Sec(duration);
    }

}
