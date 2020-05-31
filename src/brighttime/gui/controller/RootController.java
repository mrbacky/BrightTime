package brighttime.gui.controller;

import brighttime.BrightTime;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskEntry;
import brighttime.be.User;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import brighttime.gui.util.ToolTipManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author rados
 */
public class RootController implements Initializable {

    private final String TIME_TRACKER_MODULE = "/brighttime/gui/view/TimeTracker.fxml";
    private final String ADMIN_MENU_MODULE = "/brighttime/gui/view/Creator.fxml";
    private final String ADMIN_CLIENTS_MODULE = "/brighttime/gui/view/ManageClients.fxml";
    private final String ADMIN_PROJECTS_MODULE = "/brighttime/gui/view/ManageProjects.fxml";
    private final String OVERVIEW_MODULE = "/brighttime/gui/view/Overview_1.fxml";
    private final String LOGIN_VIEW = "/brighttime/gui/view/Login.fxml";
    private final String APP_ICON = "/brighttime/gui/view/assets/sun_48px.png";
    private final String ADMIN_USERS_MODULE = "/brighttime/gui/view/ManageUsers.fxml";

    @FXML
    private AnchorPane anchorPaneRoot;
    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private JFXButton btnTimeTracker;
    @FXML
    private JFXButton btnOverview;
    @FXML
    private JFXNodesList nodesList;
    @FXML
    private JFXButton btnCreator;
    @FXML
    private JFXButton btnClients;
    @FXML
    private JFXButton btnProjects;
    @FXML
    private JFXButton btnUsers;
    private IMainModel mainModel;
    private User user;
    private final AlertManager alertManager;
    private final ToolTipManager toolTipManager;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private VBox vBoxRootButtons;

    public RootController() {
        alertManager = new AlertManager();
        toolTipManager = new ToolTipManager();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    private void setUser() {
        user = mainModel.getUser();
        if (user.getType().equals(User.UserType.STANDARD)) {
            Node nodeList = vBoxRootButtons.getChildren().get(2);
            vBoxRootButtons.getChildren().remove(nodeList);
        }
    }

    public void initializeView() {

        setUser();

        loadModule(TIME_TRACKER_MODULE);
        setToolTipsForButtons();
        displayAdminMenuItems();
//        showAdminMenu();
//        hideAdminMenu();
        showAdminClientModule();
        showAdminProjectModule();
        showAdminUserModule();
//        taskMapListener = (MapChangeListener.Change<? extends LocalDate, ? extends List<TaskConcrete1>> change) -> {
//            System.out.println("adding");
//            controller.initTasks();
//        };

    }
//    boolean isListening = false;
//    private MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener;

//          Multithreading
//          ON DESTRUCT, finialize ----------------------------------------------------------------------------------------------------------
//    public void createTaskMapListener() {
//
//        if (isListening == true) {
//            mainModel.getTaskMap().addListener(taskMapListener);
//            System.out.println("adding");
//        } else {
//            mainModel.getTaskMap().removeListener(taskMapListener);
//            System.out.println("removing");
//        }
//
//    }
    TimeTrackerController controller;

    public void loadModule(String module) {
        try {
//            System.gc();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(BrightTime.class.getResource(module));
            Parent root = fxmlLoader.load();

            if (module.equals(TIME_TRACKER_MODULE)) {
                controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
//                isListening = true;
            } else if (module.equals(ADMIN_MENU_MODULE)) {
                CreatorController controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
                controller.setContr(this);
//                isListening = false;
            } else if (module.equals(ADMIN_CLIENTS_MODULE)) {
                ManageClientsController controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
//                isListening = false;
            } else if (module.equals(ADMIN_PROJECTS_MODULE)) {
                ManageProjectsController controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
                //                isListening = false;
            } else if (module.equals(ADMIN_USERS_MODULE)) {
                ManageUsersController controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
            } else if (module.equals(OVERVIEW_MODULE)) {
                OverviewController controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
//                isListening = false;
            }
//            createTaskMapListener();
            rootBorderPane.setCenter(root);
        } catch (IOException ex) {
            alertManager.showAlert("Could not load module.", "An error occured:" + ex.getMessage());
        }
    }

    @FXML
    private void loadTimeTrackerModule(ActionEvent event) {
        loadModule(TIME_TRACKER_MODULE);
    }

    @FXML
    private void loadCreatorModule(ActionEvent event) {
        loadModule(ADMIN_MENU_MODULE);
    }

    void loadAdminClientModule() {
        loadModule(ADMIN_CLIENTS_MODULE);
    }

    void loadAdminProjectModule() {
        loadModule(ADMIN_PROJECTS_MODULE);
    }

    void displayAdminMenuItems() {
        nodesList.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                nodesList.animateList();
            } else {
                nodesList.animateList(false);
            }
        });
    }

    void showAdminMenu() {
        nodesList.setOnMouseEntered((event) -> {
            nodesList.animateList();
        });
    }

    void hideAdminMenu() {
        nodesList.setOnMouseExited((event) -> {
            nodesList.animateList(false);
        });
    }

    private void showAdminClientModule() {
        btnClients.setOnAction((event) -> {
            loadAdminClientModule();
        });
    }

    private void showAdminProjectModule() {
        btnProjects.setOnAction((event) -> {
            loadAdminProjectModule();
        });
    }

    @FXML
    private void loadOverviewModule(ActionEvent event) {
        loadModule(OVERVIEW_MODULE);
    }

    private void setToolTipsForButtons() {
        toolTipManager.setToolTipForOneButton(btnTimeTracker, "Time Tracker");
        toolTipManager.setToolTipForOneButton(btnOverview, "Overview");
        toolTipManager.setToolTipForOneButton(btnCreator, "Administration");
        toolTipManager.setToolTipForOneButton(btnClients, "Manage Clients");
        toolTipManager.setToolTipForOneButton(btnProjects, "Manage Projects");
        toolTipManager.setToolTipForOneButton(btnLogout, "Logout");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        boolean logout = alertManager.showConfirmation("Logout confirmation", "Are you sure you want to log out?");
        if (logout) {
            try {
                Stage logOutStage;
                logOutStage = (Stage) btnLogout.getScene().getWindow();
                logOutStage.close();
                Image icon = new Image(getClass().getResourceAsStream(APP_ICON));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LOGIN_VIEW));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.getIcons().add(icon);
                stage.setTitle("BrightTime");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                //TODO: Exceptions for logout.
                Logger.getLogger(RootController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void loadAdminUserModule() {
        loadModule(ADMIN_USERS_MODULE);
    }

    private void showAdminUserModule() {
        btnUsers.setOnAction((ActionEvent event) -> {
            loadAdminUserModule();
        });
    }

}
