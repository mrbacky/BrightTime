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

    void getProjects(Client client) throws ModelException;

    ObservableList<Project> getProjectList();

}
