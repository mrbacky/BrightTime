package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

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
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem menuItemDeleteClient;

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
        initTableView();
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

    private void initTableView() {
        initColumns();
        tblClients.setItems(mainModel.getClientList());
    }

    private void initColumns() {
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colRate.setCellValueFactory(cellData -> cellData.getValue().hourlyRateProperty().asObject());

        enableEditableCols();
    }

    private void enableEditableCols() {
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit((TableColumn.CellEditEvent<Client, String> e) -> {
            Client selectedClient = e.getTableView().getItems().get(e.getTablePosition().getRow());
            selectedClient.setName(e.getNewValue());
            updateClient(selectedClient);
        });

        colRate.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colRate.setOnEditCommit((TableColumn.CellEditEvent<Client, Integer> e) -> {
            Client selectedClient = e.getTableView().getItems().get(e.getTablePosition().getRow());
            selectedClient.setHourlyRate(e.getNewValue());
            updateClient(selectedClient);
        });
    }

    private void updateClient(Client selectedClient) {
        try {
            mainModel.updateClient(selectedClient);
        } catch (ModelException ex) {
            Logger.getLogger(ManageClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleDeleteClient(ActionEvent event) {
        Client c = tblClients.getSelectionModel().getSelectedItem();
        boolean deleteClient = alertManager.showConfirmation("Deleting client " + c.getName(),
                "Are you sure you want to delete the client?");

        if (c != null && deleteClient) {
            try {
                mainModel.deleteClient(c);
            } catch (ModelException ex) {
                Logger.getLogger(ManageClientsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
