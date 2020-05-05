package brighttime.gui.model.concretes;

import brighttime.be.Client;
import brighttime.bll.BllFacade;
import brighttime.bll.BllManager;
import brighttime.bll.BllException;
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

    public ClientModel(BllFacade bllManager) throws IOException {
        this.bllManager = bllManager;
    }

    @Override
    public Client addClient(Client client) throws ModelException {
        try {
            return bllManager.createClient(client);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public void loadClients() throws ModelException {
        try {
            List<Client> allClients = bllManager.getClients();
            clientList.clear();
            clientList.addAll(allClients);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public ObservableList<Client> getClientList() {
        return clientList;
    }

}
