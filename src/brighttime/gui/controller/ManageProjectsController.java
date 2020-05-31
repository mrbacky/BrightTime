package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.gui.model.ModelException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class ManageProjectsController implements Initializable {

    private final String CREATE_PROJECT_FXML = "/brighttime/gui/view/CreateProject.fxml";

    @FXML
    private StackPane stackPane;
    @FXML
    private TableView<Project> tblProjects;
    @FXML
    private TableColumn<Project, String> colName;
    @FXML
    private TableColumn<Project, Client> colClient;
    @FXML
    private TableColumn<Project, Integer> colRate;

    private final AlertManager alertManager;
    private IMainModel mainModel;

    public ManageProjectsController() {
        this.alertManager = new AlertManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    public void initializeView() {
        setUpProjectCreator();
        setTable();
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
            alertManager.showAlert("Could not set up project creator.", "An error occured: " + ex.getMessage());
        }
    }

    private void setTable() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("client"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        tblProjects.setItems(mainModel.getProjectList());
        try {
            mainModel.loadAllProjects();
        } catch (ModelException ex) {
            alertManager.showAlert("Could not set up project table.", "An error occured: " + ex.getMessage());

        }
    }

}
