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
     * Creates a new client in the database.
     *
     * @param client The client to be created.
     * @return The created client.
     * @throws DalException
     */
    Client createClient(Client client) throws DalException;

    /**
     * Gets the clients from the database.
     *
     * @return A list of clients.
     * @throws DalException
     */
    List<Client> getClients() throws DalException;

    /**
     * Updates a client in the database.
     *
     * @param client The client to update.
     * @throws DalException
     */
    void updateClient(Client client) throws DalException;

    /**
     * Deletes a client.
     *
     * @param client The client to delete.
     * @return The deleted client.
     * @throws DalException
     */
    Client deleteClient(Client client) throws DalException;

}
