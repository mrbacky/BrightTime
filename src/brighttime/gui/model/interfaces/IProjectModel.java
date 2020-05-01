package brighttime.gui.model.interfaces;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.gui.model.ModelException;
import javafx.collections.ObservableList;

/**
 *
 * @author annem
 */
public interface IProjectModel {

    /**
     * Adds a new project.
     *
     * @param project The new project.
     * @return The created project from the database.
     * @throws ModelException
     */
    Project addProject(Project project) throws ModelException;

    /**
     * Gets the projects for a selected client and adds them to an
     * ObservableList.
     *
     * @param client The selected client.
     * @throws ModelException
     */
    void loadProjects(Client client) throws ModelException;

    /**
     * Gets the ObservableList containing projects.
     *
     * @return The ObservableList of projects.
     */
    ObservableList<Project> getProjectList();

}
