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

    public Client addClient(Client client) throws ModelException;

}
