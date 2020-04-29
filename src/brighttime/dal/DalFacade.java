package brighttime.dal;

import brighttime.be.Client;
import java.util.List;

/**
 *
 * @author rados
 */
public interface DalFacade {

    public List<Client> getClients() throws DalException;

}
