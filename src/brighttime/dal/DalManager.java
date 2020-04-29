package brighttime.dal;

import brighttime.be.Client;
import brighttime.dal.dao.concretes.ClientDAO;
import brighttime.dal.dao.interfaces.IClientDAO;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author rado
 */
public class DalManager implements DalFacade {

    private final IClientDAO clientDAO;

    public DalManager() throws IOException {
        clientDAO = new ClientDAO();
    }

    @Override
    public List<Client> getClients() throws DalException {
        try {
            return clientDAO.getClients();
        } catch (DalException ex) {
            throw new DalException("Could not get the clients. " + ex.getMessage());
        }
    }

}
