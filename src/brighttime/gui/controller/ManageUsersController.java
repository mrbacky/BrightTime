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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

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
    private TableView<User> tbvUsers;
    @FXML
    private AnchorPane apManageUsers;
    @FXML
    private ToggleButton btnExpandCreateUser;
    @FXML
    private VBox vboxCreateUsers;

    private IMainModel mainModel;
    @FXML
    private TableColumn<User, String> colFirstName;
    @FXML
    private TableColumn<User, String> colLastName;
    @FXML
    private TableColumn<User, String> colUserName;
    @FXML
    private TableColumn<User, String> colUserType;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem menuItemDeleteUser;
    private final AlertManager alertManager;

    public ManageUsersController() {
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

    public void initializeView() {
        loadUsers();
        initTableView();
        setupTableViewListener();

    }

    private void loadUsers() {
        try {
            mainModel.loadUsers();
        } catch (ModelException ex) {
            Logger.getLogger(ManageUsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTableView() {
        initCols();
        tbvUsers.setItems(mainModel.getUserList());

    }

    private void initCols() {
        colFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        colUserName.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        colUserType.setCellValueFactory(cellData -> cellData.getValue().userTypeProperty());

        enableEditableCollumns();
    }

    private void enableEditableCollumns() {
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit((TableColumn.CellEditEvent<User, String> e) -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setFirstName(e.getNewValue());
            User updatedUser = tbvUsers.getItems().get(e.getTablePosition().getRow());
            updateUserDetails(updatedUser);
        });

        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setOnEditCommit((TableColumn.CellEditEvent<User, String> e) -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setLastName(e.getNewValue());
            User updatedUser = tbvUsers.getItems().get(e.getTablePosition().getRow());
            updateUserDetails(updatedUser);
        });

        colUserName.setCellFactory(TextFieldTableCell.forTableColumn());
        colUserName.setOnEditCommit((TableColumn.CellEditEvent<User, String> e) -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setUsername(e.getNewValue());
            User updatedUser = tbvUsers.getItems().get(e.getTablePosition().getRow());
            updateUserDetails(updatedUser);
        });

        colUserType.setCellFactory(TextFieldTableCell.forTableColumn());
        colUserType.setOnEditCommit((TableColumn.CellEditEvent<User, String> e) -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setType(User.UserType.valueOf(e.getNewValue()));
            User updatedUser = tbvUsers.getItems().get(e.getTablePosition().getRow());
            updateUserDetails(updatedUser);
            System.out.println("in usertype col");
        });

    }

    @FXML
    private void handleExpandCreateUser(ActionEvent event) {
        if (btnExpandCreateUser.isSelected()) {
            expandCreateUser();
        } else {
            collapseCreateUser();
        }

    }

    private void expandCreateUser() {
        imgExpandCollapse.setImage(COLLAPSE_ICON_IMAGE);
        setUpUserCreator();
    }

    public void collapseCreateUser() {
        imgExpandCollapse.setImage(EXPAND_ICON_IMAGE);
        vboxCreateUsers.getChildren().clear();
        btnExpandCreateUser.setSelected(false);
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

    private void setupTableViewListener() {

//        mainModel.getUserList().addListener((ListChangeListener.Change<? extends User> c) -> {
//            System.out.println("update");
//
//        });
    }

    private void updateUserDetails(User updatedUser) {
        try {
            mainModel.updateUserDetails(updatedUser);
        } catch (ModelException ex) {
            Logger.getLogger(ManageUsersController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        User u = tbvUsers.getSelectionModel().getSelectedItem();
        boolean deleteUser = alertManager.showConfirmation("Deleting User " + u.getFirstName() + " " + u.getLastName(),
                "Are you sure you want to delete the user?");

        if (u != null && deleteUser) {
            try {
                mainModel.deleteUser(u);
            } catch (ModelException ex) {
                alertManager.showAlert("Could not delete the user.", "An error occured: " + ex.getMessage());

            }
        }

    }

}
