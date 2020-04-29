package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Task;
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
    public List<Client> getClients() throws LogicException {
        try {
            return dalManager.getClients();
        } catch (DalException ex) {
            throw new LogicException("Could not get the clients. " + ex.getMessage());
        }
    }

}
