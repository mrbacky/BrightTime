/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.controller;

import brighttime.be.User;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IAuthenticationModel;
import brighttime.gui.model.interfaces.IMainModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.JTree;

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
    private IAuthenticationModel authenticationModel;
    private User user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initializeView() {
        fieldValidator();

    }

    @FXML
    private void EnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            try {
                authenticateUser();
            } catch (Exception ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    private void loginBtnPressed(ActionEvent event) {
        try {
            authenticateUser();
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
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
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void authenticateUser() throws Exception {
        user = authenticationModel.authenticateUser(txtUsername.getText(), txtPassword.getText());
        if (user != null) {
            initializeRoot(user);
            closeLogin();

        }

    }

    public void injectAuthenticationModel(IAuthenticationModel authenticationModel) {
        this.authenticationModel = authenticationModel;

    }

    private void closeLogin() {
        Stage loginStage;
        loginStage = (Stage) btnLogIn.getScene().getWindow();
        loginStage.close();
    }

}
