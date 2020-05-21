/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.controller;

import brighttime.be.User;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class CreateUserController implements Initializable {

    @FXML
    private JFXTextField textFieldFirstName;
    @FXML
    private JFXTextField textFieldPassword;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXTextField textFieldUserName;
    @FXML
    private JFXTextField textFieldLastName;
    private IMainModel mainModel;
    private final AlertManager alertManager;

    public CreateUserController() {
        this.alertManager = new AlertManager();
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
        handleCreateUser();
    }

    private void handleCreateUser() {
        btnAdd.setOnAction((ActionEvent event) -> {

            try {
                String firstName = textFieldFirstName.getText();
                String lastName = textFieldLastName.getText();
                String password = textFieldPassword.getText();
                String userName = textFieldUserName.getText();

                mainModel.createUser(new User(firstName, lastName, userName, password, User.UserType.USER));
            } catch (ModelException ex) {
                alertManager.showAlert("Could not create the user.", "An error occured: " + ex.getMessage());
            }

        });

    }

}
