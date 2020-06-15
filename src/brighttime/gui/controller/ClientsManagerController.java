package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class ClientsManagerController implements Initializable {

    private final String CREATE_CLIENT_FXML = "/brighttime/gui/view/ClientCreator.fxml";

    @FXML
    private StackPane stackPane;
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

    private final AlertManager alertManager;
    private IMainModel mainModel;

    public ClientsManagerController() {
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
            ClientCreatorController controller = fxmlLoader.getController();
            controller.injectManageClientsController(this);
            controller.injectMainModel(mainModel);
            controller.initializeView();
            stackPane.getChildren().add(root);
        } catch (IOException ex) {
            alertManager.showAlert("Could not set up client creator.", "Error: " + ex.getMessage());
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
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
            Client selectedClient = tblClients.getItems().get(e.getTablePosition().getRow());
            updateClient(selectedClient);
        });

        colRate.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colRate.setOnEditCommit((TableColumn.CellEditEvent<Client, Integer> e) -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setHourlyRate(e.getNewValue());
            Client selectedClient = e.getTableView().getItems().get(e.getTablePosition().getRow());
            updateClient(selectedClient);
        });
    }

    private void updateClient(Client selectedClient) {
        try {
            mainModel.updateClient(selectedClient);
        } catch (ModelException ex) {
            alertManager.showAlert("Unable to update the client.", "An error occured: " + ex.getMessage());
        }
    }

    @FXML
    private void handleDeleteClient(ActionEvent event) {
        Client client = tblClients.getSelectionModel().getSelectedItem();
        boolean deleteClient = alertManager.showConfirmation("Deleting client " + client.getName(), "Are you sure you want to delete the client?");
        if (deleteClient) {
            try {
                mainModel.deleteClient(client);
            } catch (ModelException ex) {
                alertManager.showAlert("Could not delete the client.", "An error occured: " + ex.getMessage());
            }
        }

    }

}
