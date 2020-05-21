/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.controller;

import brighttime.gui.model.interfaces.IMainModel;
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
 * @author rados
 */
public class ManageUsersController implements Initializable {

    private final String CREATE_USER_FXML = "/brighttime/gui/view/CreateUser.fxml";

    @FXML
    private StackPane stackPane;
    @FXML
    private VBox vBox;
    private IMainModel mainModel;

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
        setUpUserCreator();
    }

    private void setUpUserCreator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(CREATE_USER_FXML));

            Parent root = fxmlLoader.load();
            CreateUserController controller = fxmlLoader.getController();
            controller.injectMainModel(mainModel);
            controller.initializeView();
            stackPane.getChildren().add(root);

        } catch (IOException ex) {

        }
    }
}
