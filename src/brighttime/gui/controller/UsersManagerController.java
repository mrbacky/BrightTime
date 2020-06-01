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
import brighttime.gui.util.InputValidator;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class UsersManagerController implements Initializable {

    private final Image EXPAND_ICON_IMAGE = new Image("/brighttime/gui/view/assets/expand.png");
    private final Image COLLAPSE_ICON_IMAGE = new Image("/brighttime/gui/view/assets/collapse.png");
    private final String CREATE_USER_FXML = "/brighttime/gui/view/UserCreator.fxml";

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
    @FXML
    private TableColumn<User, String> colFirstName;
    @FXML
    private TableColumn<User, String> colLastName;
    @FXML
    private TableColumn<User, String> colUserName;
    @FXML
    private TableColumn<User, User.UserType> colUserType;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem menuItemDeleteUser;

    private final AlertManager alertManager;
    private final InputValidator inputValidator;

    private IMainModel mainModel;

    public UsersManagerController() {
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

    public void initializeView() {
        loadUsers();
        initTableView();
    }

    private void loadUsers() {
        try {
            mainModel.loadUsers();
        } catch (ModelException ex) {
            alertManager.showAlert("Could not load users.", "An error occured: " + ex.getMessage());
        }
    }

    public void initTableView() {
        tbvUsers.getItems().clear();
        initCols();
        tbvUsers.setItems(mainModel.getUserList());
    }

    private void initCols() {
        colFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        colUserName.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        colUserType.setCellValueFactory((TableColumn.CellDataFeatures<User, User.UserType> param) -> {
            User user = param.getValue();
            return new SimpleObjectProperty<>(user.getType());
        });

        setupEditableColumns();
    }

    public void setupEditableColumns() {
        setupFirstNameCol();
        setupLastNameCol();
        setupUsernameCol();
        setupUsertypeCol();
    }

    private void setupFirstNameCol() {
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit((TableColumn.CellEditEvent<User, String> e) -> {
            User u = tbvUsers.getItems().get(e.getTablePosition().getRow());
            String oldFirstName = e.getOldValue();
            try {
                u.setFirstName(e.getNewValue());
                mainModel.updateUserDetails(u);
            } catch (ModelException ex) {
                Platform.runLater(() -> {
                    u.setFirstName(oldFirstName);
                    e.getTableView().getColumns().get(0).setVisible(false);
                    e.getTableView().getColumns().get(0).setVisible(true);
                    alertManager.showAlert("Could not edit user's first name", "Error: " + ex);
                });
            }
        });
    }

    private void setupLastNameCol() {
        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setOnEditCommit((TableColumn.CellEditEvent<User, String> e) -> {
            User u = tbvUsers.getItems().get(e.getTablePosition().getRow());
            String oldLastName = e.getOldValue();
            try {
                u.setLastName(e.getNewValue());
                mainModel.updateUserDetails(u);
            } catch (ModelException ex) {
                Platform.runLater(() -> {
                    u.setLastName(oldLastName);
                    e.getTableView().getColumns().get(1).setVisible(false);
                    e.getTableView().getColumns().get(1).setVisible(true);
                    alertManager.showAlert("Could not edit user's last name", "Error: " + ex);
                });
            }
        });

    }

    private void setupUsernameCol() {
        colUserName.setCellFactory(TextFieldTableCell.forTableColumn());
        colUserName.setOnEditCommit((TableColumn.CellEditEvent<User, String> e) -> {
            User u = tbvUsers.getItems().get(e.getTablePosition().getRow());
            String oldUserName = e.getOldValue();
            if (inputValidator.usernameCheck(u.getUsername())) {
                try {
                    u.setUsername(e.getNewValue());
                    mainModel.updateUserDetails(u);
                } catch (ModelException ex) {
                    Platform.runLater(() -> {
                        u.setUsername(oldUserName);
                        e.getTableView().getColumns().get(2).setVisible(false);
                        e.getTableView().getColumns().get(2).setVisible(true);
                        alertManager.showAlert("Could not edit user name", "Error: " + ex.getMessage());
                    });
                }
            } else {
                Platform.runLater(() -> {
                    u.setUsername(oldUserName);
                    e.getTableView().getColumns().get(2).setVisible(false);
                    e.getTableView().getColumns().get(2).setVisible(true);
                    alertManager.showAlert("The username is invalid.", "Please try another username.");
                });

            }
        });
    }

    private void setupUsertypeCol() {
        ObservableList<User.UserType> userTypes = FXCollections.observableArrayList(User.UserType.values());
        colUserType.setCellFactory(ComboBoxTableCell.forTableColumn(userTypes));
        colUserType.setOnEditCommit((TableColumn.CellEditEvent<User, User.UserType> e) -> {
            User u = e.getTableView().getItems().get(e.getTablePosition().getRow());
            User.UserType oldUserType = e.getOldValue();
            try {
                u.setType(e.getNewValue());
                mainModel.updateUserDetails(u);
            } catch (ModelException ex) {
                Platform.runLater(() -> {
                    u.setType(oldUserType);
                    e.getTableView().getColumns().get(3).setVisible(false);
                    e.getTableView().getColumns().get(3).setVisible(true);
                    alertManager.showAlert("Could not edit user type.", "Error: " + ex.getMessage());
                });
            }
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
            UserCreatorController controller = fxmlLoader.getController();
            controller.injectMainModel(mainModel);
            controller.injectContr(this);
            controller.initializeView();
            vboxCreateUsers.getChildren().add(root);
        } catch (IOException ex) {
            alertManager.showAlert("Could not set up the user creator.", "An error occured: " + ex.getMessage());
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
