package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.gui.util.AlertManager;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.ValidationManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author annem
 */
public class CreateProjectController implements Initializable {

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXComboBox<Client> cboClient;
    @FXML
    private JFXTextField txtHourlyRate;
    @FXML
    private JFXButton btnAdd;

    private IMainModel mainModel;
    private ManageProjectsController contr;
    private final AlertManager alertManager;
    private final ValidationManager validationManager;

    public CreateProjectController() {
        this.alertManager = new AlertManager();
        this.validationManager = new ValidationManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void injectManageProjectsController(ManageProjectsController contr) {
        this.contr = contr;
    }

    public void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    void initializeView() throws IOException {
        System.out.println("in CreateProject page");
        setClientsIntoComboBox();
        setValidators();
        addProject();
    }

    /**
     * Sets the clients into the ComboBox.
     */
    private void setClientsIntoComboBox() {
        if (mainModel.getClientList() != null) {
            try {
                mainModel.loadClients();
                cboClient.getItems().clear();
                cboClient.getItems().addAll(mainModel.getClientList());
            } catch (ModelException ex) {
                alertManager.showAlert("Could not get the clients.", "An error occured: " + ex.getMessage());
            }
        }
    }

    /**
     * Sets all validator.
     */
    private void setValidators() {
        validationManager.inputValidation(txtName, "No name written.");
        validationManager.comboBoxValidation(cboClient, "No client selected.");
        validationManager.inputValidation(txtHourlyRate, "Optional.");
    }

    /**
     * Adds a new project.
     */
    private void addProject() {
        btnAdd.setOnAction((event) -> {
            if (!txtName.getText().trim().isEmpty() && !cboClient.getSelectionModel().isEmpty()) {
                try {
                    int hourlyRate = 0;
                    if (!txtHourlyRate.getText().trim().isEmpty()) {
                        hourlyRate = Integer.parseInt(txtHourlyRate.getText().trim());
                    }
                    mainModel.addProject(new Project(
                            txtName.getText().trim(),
                            cboClient.getSelectionModel().getSelectedItem(),
                            hourlyRate)
                    );
                    System.out.println("Action event is working!");
                } catch (ModelException ex) {
                    alertManager.showAlert("Could not create the project.", "An error occured: " + ex.getMessage());
                }
            } else if (txtName.getText().trim().isEmpty()) {
                alertManager.showAlert("No project name was entered.", "Please enter a name for the new project.");
            } else {
                alertManager.showAlert("No client is selected.", "Please select a client.");
            }
        });
    }

}
