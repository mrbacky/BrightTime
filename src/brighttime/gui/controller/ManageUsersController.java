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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class ManageUsersController implements Initializable {

    private final Image EXPAND_ICON_IMAGE = new Image("/brighttime/gui/view/assets/expand.png");
    private final Image COLLAPSE_ICON_IMAGE = new Image("/brighttime/gui/view/assets/collapse.png");
    private final String CREATE_USER_FXML = "/brighttime/gui/view/CreateUser.fxml";

    @FXML
    private VBox vBox;
    @FXML
    private ImageView imgExpandCollapse;
    @FXML
    private ScrollPane spManageUsers;
    @FXML
    private TableView<?> tbvUsers;
    @FXML
    private AnchorPane apManageUsers;
    @FXML
    private ToggleButton btnExpandCreateUser;
    @FXML
    private VBox vboxCreateUsers;

    private IMainModel mainModel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;

    }

    void initializeView() {

    }

    public void setUpUserCreator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(CREATE_USER_FXML));

            Parent root = fxmlLoader.load();
            CreateUserController controller = fxmlLoader.getController();
            controller.injectMainModel(mainModel);
            controller.injectContr(this);
            controller.initializeView();
//            vboxCreateUsers.getChildren().clear();
            vboxCreateUsers.getChildren().add(root);
        } catch (IOException ex) {

        }
    }

    @FXML
    private void handleExpandCreateUser(ActionEvent event) {
        if (btnExpandCreateUser.isSelected()) {
            expandCreateUser();
        } else {
            collapseCreateUser();
        }

    }

    public void collapseCreateUser() {
        imgExpandCollapse.setImage(EXPAND_ICON_IMAGE);
        vboxCreateUsers.getChildren().clear();
        btnExpandCreateUser.setSelected(false);
    }

    private void expandCreateUser() {
        imgExpandCollapse.setImage(COLLAPSE_ICON_IMAGE);
        setUpUserCreator();
    }

}
