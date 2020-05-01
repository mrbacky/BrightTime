package brighttime.gui.model.interfaces;

import brighttime.be.Client;
import brighttime.gui.model.ModelException;
import javafx.collections.ObservableList;

/**
 *
 * @author annem
 */
public interface IClientModel {

    /**
     * Adds a new client.
     *
     * @param client The new client.
     * @return The created client from the database.
     * @throws ModelException
     */
    Client addClient(Client client) throws ModelException;

    /**
     * Gets the clients and adds them to an ObservableList.
     *
     * @throws ModelException
     */
    void loadClients() throws ModelException;

    /**
     * Gets the ObservableList containing clients.
     *
     * @return The ObservableList of clients.
     */
    ObservableList<Client> getClientList();

}
