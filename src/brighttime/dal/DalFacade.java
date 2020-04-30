package brighttime.dal;

import brighttime.be.Client;
import brighttime.be.Project;
import java.util.List;

/**
 *
 * @author rados
 */
public interface DalFacade {

    public List<Client> getClients() throws DalException;
    
    public List<Project> getProjects(Client client) throws DalException;

}
