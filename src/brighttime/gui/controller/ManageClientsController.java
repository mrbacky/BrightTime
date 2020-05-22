package brighttime.gui.controller;

import brighttime.be.Client;
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
public class ManageClientsController implements Initializable {

    @FXML
    private StackPane stackPane;

    private final String CREATE_CLIENT_FXML = "/brighttime/gui/view/CreateClient.fxml";
    private IMainModel mainModel;
    private final AlertManager alertManager;
    @FXML
    private TableView<Client> tblClients;
    @FXML
    private TableColumn<Client, String> colName;
    @FXML
    private TableColumn<Client, Integer> colRate;

    public ManageClientsController() {
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
        setUpClientCreator();
        setTable();
    }

    private void setUpClientCreator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(CREATE_CLIENT_FXML));

            Parent root = fxmlLoader.load();
            CreateClientController controller = fxmlLoader.getController();
            controller.injectManageClientsController(this);
            controller.injectMainModel(mainModel);
            controller.initializeView();
            stackPane.getChildren().add(root);

        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTable() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        tblClients.setItems(mainModel.getClientList());
    }

}
