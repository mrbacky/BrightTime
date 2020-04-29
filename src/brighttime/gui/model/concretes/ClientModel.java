package brighttime.gui.model.concretes;

import brighttime.be.Client;
import brighttime.bll.BllFacade;
import brighttime.bll.BllManager;
import brighttime.bll.LogicException;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IClientModel;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author annem
 */
public class ClientModel implements IClientModel {

    private final BllFacade bllManager;
    private final ObservableList<Client> clientList = FXCollections.observableArrayList();

    public ClientModel() throws IOException {
        bllManager = new BllManager();
    }

    @Override
    public void getClients() throws ModelException {
        try {
            List<Client> allClients = bllManager.getClients();
            clientList.clear();
            clientList.addAll(allClients);
        } catch (LogicException ex) {
            throw new ModelException("Could not get the clients. " + ex.getMessage());
        }
    }

    @Override
    public ObservableList<Client> getClientList() {
        return clientList;
    }

}
