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
     * Creates a new project in the database.
     *
     * @param project The project to be created.
     * @return The created project.
     * @throws DalException
     */
    Project createProject(Project project) throws DalException;

    /**
     * Gets the projects for a selected client.
     *
     * @param client The selected client.
     * @return A list of projects.
     * @throws DalException
     */
    List<Project> getProjectsForAClient(Client client) throws DalException;

    /**
     * Gets all the projects from the database.
     *
     * @return A list of all projects.
     * @throws DalException
     */
    List<Project> getAllProjects() throws DalException;

}
