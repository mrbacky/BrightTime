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

    List<Project> getProjects(Client client) throws DalException;

}
