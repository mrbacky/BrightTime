package brighttime.gui.controller;

import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class ManageProjectsController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private VBox vBox;

    private final String CREATE_PROJECT_FXML = "/brighttime/gui/view/CreateProject.fxml";
    private IMainModel mainModel;
    private final AlertManager alertManager;

    public ManageProjectsController() {
        this.alertManager = new AlertManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    public void initializeView() {
        setUpProjectCreator();
    }

    private void setUpProjectCreator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(CREATE_PROJECT_FXML));

            Parent root = fxmlLoader.load();
            CreateProjectController controller = fxmlLoader.getController();
            controller.injectManageProjectsController(this);
            controller.injectMainModel(mainModel);
            controller.initializeView();
            stackPane.getChildren().add(root);

        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
