package brighttime.gui.model.interfaces;

import brighttime.be.Client;
import brighttime.gui.model.ModelException;
import javafx.collections.ObservableList;

/**
 *
 * @author annem
 */
public interface IClientModel {

    void getClients() throws ModelException;

    ObservableList<Client> getClientList();

}
