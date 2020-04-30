package brighttime.dal.dao.interfaces;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.dal.DalException;
import java.util.List;

/**
 *
 * @author annem
 */
public interface IProjectDAO {

    /**
     * Gets the projects for a selected client.
     *
     * @param client The selected client.
     * @return A list of projects.
     * @throws DalException
     */
    List<Project> getProjects(Client client) throws DalException;

}
