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
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.ComboBoxTableCell;
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
    private TableColumn<User, User.UserType> colUserType;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem menuItemDeleteUser;
    private final AlertManager alertManager;
    private final InputValidator inputValidator;

    public ManageUsersController() {
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
        setupTableViewListener();

    }

    private void loadUsers() {
        try {
            mainModel.loadUsers();
        } catch (ModelException ex) {
            Logger.getLogger(ManageUsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initTableView() {
        initCols();
        tbvUsers.setItems(mainModel.getUserList());

    }

    private void initCols() {
        colFirstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        colUserName.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

//        colUserType.setCellValueFactory(cellData -> cellData.getValue().userTypeProperty().t);
        enableEditableCollumns();
    }

    private void enableEditableCollumns() {
        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit((TableColumn.CellEditEvent<User, String> e) -> {
            User u = tbvUsers.getItems().get(e.getTablePosition().getRow());

            try {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setFirstName(e.getNewValue());
                mainModel.updateUserDetails(u);
            } catch (ModelException ex) {
                initTableView();
                alertManager.showAlert("sddsd", "asdasd " + ex.getMessage());

            }
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

            if (inputValidator.usernameCheck(updatedUser.getUsername())) {
                updateUserDetails(updatedUser);
            } else {
                alertManager.showAlert("The username is invalid.", "Please try another username.");
            }

//            Søren pseudo code (I am too tired to do this... but you can try if you want.):            
//            String oldvalue = e.getNewValue();
//            User dbResult = updateUserDetails(updatedUser);
//            if (dbResult == null) //was not allowed
//               e.getTableView().getItems().get(e.getTablePosition().getRow()).setUsername(oldvalue);
//            
//            updateUserDetails(updatedUser);
        });

        ObservableList<User.UserType> userTypes = FXCollections.observableArrayList(User.UserType.values());
        colUserType.setCellValueFactory((TableColumn.CellDataFeatures<User, User.UserType> param) -> {
            User user = param.getValue();
            return new SimpleObjectProperty<User.UserType>(user.getType());
        });

        colUserType.setCellFactory(ComboBoxTableCell.forTableColumn(userTypes));
        colUserType.setOnEditCommit((TableColumn.CellEditEvent<User, User.UserType> e) -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setType(e.getNewValue());
//            TablePosition<User, User.UserType> pos = event.getTablePosition();
            User updatedUser = tbvUsers.getItems().get(e.getTablePosition().getRow());
            updateUserDetails(updatedUser);

        });

//        colUserType.setCellFactory(TextFieldTableCell.forTableColumn());
//        colUserType.setOnEditCommit((TableColumn.CellEditEvent<User, String> e) -> {
//            e.getTableView().getItems().get(e.getTablePosition().getRow()).setType(User.UserType.valueOf(e.getNewValue()));
//            User updatedUser = tbvUsers.getItems().get(e.getTablePosition().getRow());
//            updateUserDetails(updatedUser);
//            System.out.println("in usertype col");
//        });
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
            alertManager.showAlert("Could not update the new user details.", "An error occured: " + ex.getMessage());
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
