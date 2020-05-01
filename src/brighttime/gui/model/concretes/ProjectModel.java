package brighttime.gui.model.concretes;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.bll.BllFacade;
import brighttime.bll.BllManager;
import brighttime.bll.BllException;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IProjectModel;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author annem
 */
public class ProjectModel implements IProjectModel {

    private final BllFacade bllManager;
    private final ObservableList<Project> projectList = FXCollections.observableArrayList();

    public ProjectModel() throws IOException {
        bllManager = new BllManager();
    }

    @Override
    public void loadProjects(Client client) throws ModelException {
        try {
            List<Project> allProjects = bllManager.getProjects(client);
            projectList.clear();
            projectList.addAll(allProjects);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

    @Override
    public ObservableList<Project> getProjectList() {
        return projectList;
    }

}
