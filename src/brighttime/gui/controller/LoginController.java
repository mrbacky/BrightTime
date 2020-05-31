package brighttime.gui.controller;

import brighttime.be.User;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IAuthenticationModel;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import brighttime.gui.util.InputValidator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class LoginController implements Initializable {

    private final String ROOT_PATH = "/brighttime/gui/view/Root.fxml";
    private final String APP_ICON = "/brighttime/gui/view/assets/sun_48px.png";

    @FXML
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXButton btnLogIn;
    
    private final AlertManager alertManager;
    private final InputValidator inputValidator;
    private IAuthenticationModel authenticationModel;
    private User user;

    public LoginController() throws ModelException {
        alertManager = new AlertManager();
        this.inputValidator = new InputValidator();
        try {
            authenticationModel = ModelCreator.getInstance().createAuthenticationModel();
        } catch (IOException ex) {
            alertManager.showAlert("Could not get authentication model", "An error occured: " + ex.getMessage());
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void EnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            authenticateUser();
        }
    }

    @FXML
    private void loginBtnPressed(ActionEvent event) {
        if (validateInput()) {
            authenticateUser();
        }
    }

    private void fieldValidator() {
        RequiredFieldValidator usernameValidator = new RequiredFieldValidator();
        RequiredFieldValidator passwordValidator = new RequiredFieldValidator();
        txtUsername.getValidators().add(usernameValidator);
        usernameValidator.setMessage("Please fill out username.");
        txtPassword.getValidators().add(passwordValidator);
        passwordValidator.setMessage("Please fill out password");
        txtUsername.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtUsername.validate();
            }
        });
        txtPassword.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                txtPassword.validate();
            }
        });
    }

    private void initializeRoot(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ROOT_PATH));
            Parent root = fxmlLoader.load();
            IMainModel mainModel = ModelCreator.getInstance().createMainModel();
            mainModel.setUser(user);
            RootController controller = fxmlLoader.getController();
            controller.injectMainModel(mainModel);
            controller.initializeView();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            Image icon = new Image(getClass().getResourceAsStream(APP_ICON));
            stage.getIcons().add(icon);
            stage.setTitle("BrightTime");
            stage.setMinWidth(850);
            stage.setMinHeight(400);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void authenticateUser() {
        try {
            user = authenticationModel.authenticateUser(txtUsername.getText(), txtPassword.getText());
            if (user != null) {
                Platform.runLater(() -> {
                    initializeRoot(user);
                    closeLogin();
                });
            }
        } catch (ModelException ex) {
            alertManager.showAlert("Could not authenticate user.", "An error occured: " + ex.getMessage());
        }

    }

    private void closeLogin() {
        Stage loginStage;
        loginStage = (Stage) btnLogIn.getScene().getWindow();
        loginStage.close();
    }

    private boolean validateInput() {
        if (txtUsername.getText().trim().isEmpty()) {
            alertManager.showAlert("No username was entered.", "Please enter a username.");
            return false;
        }
        if (!inputValidator.usernameCheck(txtUsername.getText())) {
            alertManager.showAlert("The username is invalid.", "Please try again.");
            return false;
        }
        if (txtPassword.getText().trim().isEmpty()) {
            alertManager.showAlert("No password was entered.", "Please enter a password.");
            return false;
        }
        if (!inputValidator.passwordCheck(txtPassword.getText())) {
            alertManager.showAlert("The password is invalid.", "Please try again.");
            return false;
        }
        return true;
    }

}
