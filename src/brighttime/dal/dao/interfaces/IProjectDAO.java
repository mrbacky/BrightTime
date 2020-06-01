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

    List<Project> getAllProjects() throws DalException;

    void updateProject(Project project) throws DalException;

    void deleteProject(Project project) throws DalException;

}
