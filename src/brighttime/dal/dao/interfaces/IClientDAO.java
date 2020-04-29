package brighttime.dal.dao.interfaces;

import brighttime.be.Client;
import brighttime.dal.DalException;
import java.util.List;

/**
 *
 * @author annem
 */
public interface IClientDAO {

    /**
     * Gets the clients.
     *
     * @return A list of clients.
     * @throws DalException
     */
    List<Client> getClients() throws DalException;
}
