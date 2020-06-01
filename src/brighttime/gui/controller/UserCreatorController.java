package brighttime.gui.controller;

import brighttime.be.User;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import brighttime.gui.util.InputValidator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class UserCreatorController implements Initializable {

    @FXML
    private JFXTextField textFieldFirstName;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXTextField textFieldUserName;
    @FXML
    private JFXTextField textFieldLastName;
    @FXML
    private JFXComboBox<String> cboUserType;
    @FXML
    private JFXButton btnClearInfo;
    @FXML
    private JFXPasswordField passField2;
    @FXML
    private JFXPasswordField passField1;
    private final AlertManager alertManager;
    private final InputValidator inputValidator;
    private IMainModel mainModel;
    private String password1;
    private String password2;
    private UsersManagerController contr;

    public UserCreatorController() {
        this.alertManager = new AlertManager();
        this.inputValidator = new InputValidator();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    void initializeView() {
        initUserTypeComboBox();

        handleCreateUser();
    }

    private void handleCreateUser() {
        btnAdd.setOnAction((ActionEvent event) -> {

            try {
                String firstName = textFieldFirstName.getText();
                String lastName = textFieldLastName.getText();
                password1 = passField1.getText();
                password2 = passField2.getText();

                String userName = textFieldUserName.getText();
                String userType = cboUserType.getValue();

                if (validateInput()) {
                    if (userType.equals("Administrator")) {
                        mainModel.createUser(new User(firstName, lastName, userName, password1, User.UserType.ADMINISTRATOR));
                    } else {
                        mainModel.createUser(new User(firstName, lastName, userName, password1, User.UserType.STANDARD));

                    }
                    handleClearInfo(event);
                    alertManager.showSuccess("User was successfully created.", firstName + " " + lastName);
                    contr.collapseCreateUser();
                }
            } catch (ModelException ex) {
                alertManager.showAlert("Could not create the user.", "An error occured: " + ex.getMessage());
            }

        });

    }

    private void initUserTypeComboBox() {
        cboUserType.getItems().add("Administrator");
        cboUserType.getItems().add("Standard");
    }

    @FXML
    private void handleClearInfo(ActionEvent event) {
        textFieldFirstName.clear();
        textFieldLastName.clear();
        passField1.clear();
        passField2.clear();
        textFieldUserName.clear();
        cboUserType.getSelectionModel().clearSelection();

    }

    private boolean validateInput() {
        if (textFieldFirstName.getText().isEmpty()) {
            alertManager.showAlert("No first name was entered.", "Please enter a first name of the new user.");
            return false;
        }
        if (textFieldLastName.getText().isEmpty()) {
            alertManager.showAlert("No last name was entered.", "Please enter a last name of the new user.");
            return false;
        }
        if (cboUserType.getSelectionModel().isEmpty()) {
            alertManager.showAlert("No user type is selected.", "Please select a user type.");
            return false;
        }
        if (textFieldUserName.getText().isEmpty()) {
            alertManager.showAlert("No username was entered.", "Please enter a username.");
            return false;
        }

        if (!inputValidator.usernameCheck(textFieldUserName.getText())) {
            alertManager.showAlert("The username is invalid.", "Please try another username.");
            return false;
        }

        if (password1.isEmpty()) {
            alertManager.showAlert("No password was entered.", "Please enter a password.");
            return false;
        }
        if (password2.isEmpty()) {
            alertManager.showAlert("No password confirmation was entered.", "Please enter a password confirmation.");
            return false;
        }
        if (!password1.equals(password2)) {
            System.out.println("p1: " + password1);
            System.out.println("p2: " + password2);

            alertManager.showAlert("The password didn't match.", "Try again.");
            return false;
        }
        if (!inputValidator.passwordCheck(password1)) {
            alertManager.showAlert("The password is invalid.", "Please enter another password, which satisfies the requirements.");
            return false;
        }
        return true;
    }

    void injectContr(UsersManagerController contr) {
        this.contr = contr;

    }

}
