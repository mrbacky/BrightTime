package brighttime.dal;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.dal.dao.concretes.ClientDAO;
import brighttime.dal.dao.concretes.ProjectDAO;
import brighttime.dal.dao.interfaces.IClientDAO;
import brighttime.dal.dao.interfaces.IProjectDAO;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author rado
 */
public class DalManager implements DalFacade {

    private final IClientDAO clientDAO;
    private final IProjectDAO projectDAO;

    public DalManager() throws IOException {
        clientDAO = new ClientDAO();
        projectDAO = new ProjectDAO();
    }

    @Override
    public List<Client> getClients() throws DalException {
        try {
            return clientDAO.getClients();
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Project> getProjects(Client client) throws DalException {
        try {
            return projectDAO.getProjects(client);
        } catch (DalException ex) {
            throw new DalException(ex.getMessage());
        }
    }

}
