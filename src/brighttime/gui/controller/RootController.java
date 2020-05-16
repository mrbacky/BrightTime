package brighttime.gui.controller;

import brighttime.BrightTime;
import brighttime.be.User;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.util.AlertManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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

    public RootController() {
        alertManager = new AlertManager();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //        loadModule(TIME_TRACKER_MODULE);
        //        setToolTipsForButtons();
        //        displayAdminMenuItems();
        //        //showAdminMenu();
        //        //hideAdminMenu();
        //        showAdminClientModule();
        //        showAdminProjectModule();
    }

    void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    private void setUser() {
        user = mainModel.getUser();
    }

    public void initializeView() {
        setUser();
        loadModule(TIME_TRACKER_MODULE);

    }

    public void loadModule(String module) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(BrightTime.class.getResource(module));
            Parent root = fxmlLoader.load();

            if (module.equals(TIME_TRACKER_MODULE)) {
                TimeTrackerController controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
            } else if (module.equals(ADMIN_MENU_MODULE)) {
                CreatorController controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
                controller.setContr(this);
            } else if (module.equals(ADMIN_CLIENTS_MODULE)) {
                ManageClientsController controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
            } else if (module.equals(ADMIN_PROJECTS_MODULE)) {
                ManageProjectsController controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
            } else if (module.equals(OVERVIEW_MODULE)) {
                OverviewController controller = fxmlLoader.getController();
                controller.injectMainModel(mainModel);
                controller.initializeView();
            }
            rootBorderPane.setCenter(root);
        } catch (IOException ex) {
            alertManager.showAlert("Could not load module.", "An error occured." + ex.getMessage());
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
        setToolTipForOneButton(btnTimeTracker, "Time Tracker");
        setToolTipForOneButton(btnOverview, "Overview");
        setToolTipForOneButton(btnCreator, "Administration");
        setToolTipForOneButton(btnClients, "Manage Clients");
        setToolTipForOneButton(btnProjects, "Manage Projects");
    }

    private void setToolTipForOneButton(JFXButton button, String tip) {
        button.setTooltip(new Tooltip(tip));
    }

}
