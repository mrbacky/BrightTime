package brighttime.bll;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.be.Task;
import java.util.List;

/**
 *
 * @author rados
 */
public interface BllFacade {

    public List<Client> getClients() throws LogicException;

    public List<Project> getProjects(Client client) throws LogicException;

    public List<Task> loadTasks();

}
