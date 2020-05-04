/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.controller;

import brighttime.BrightTime;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.ModelFacade;
import brighttime.gui.model.ModelManager;
import brighttime.gui.model.concretes.MainModel;
import brighttime.gui.model.interfaces.IMainModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author rados
 */
public class RootController implements Initializable {

    private final String TIME_TRACKER_MODULE = "/brighttime/gui/view/TimeTracker.fxml";
    private final String CREATOR_MODULE = "/brighttime/gui/view/CreateTask.fxml";
    private final String OVERVIEW_MODULE = "/brighttime/gui/view/CreateProject.fxml";

    @FXML
    private AnchorPane anchorPaneRoot;
    @FXML
    private BorderPane rootBorderPane;
    @FXML
    private Button btnTimeTracker;
    @FXML
    private Button btnCreator;
    @FXML
    private Button btnOverview;
    private IMainModel mainModel;

    public RootController() throws IOException {
        mainModel = ModelCreator.getInstance().createMainModel();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                
//            } else if (module.equals(CREATOR_MODULE)) {
//                CreateTaskController controller = fxmlLoader.getController();
////                controller.injectModelManager(modelManager);
//                controller.initializeView();               
//            } else if (module.equals(OVERVIEW_MODULE)) {
//                CreateProjectController controller = fxmlLoader.getController();
////                controller.injectModelManager(modelManager);
//                controller.initializeView();
            }
            rootBorderPane.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(RootController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void loadTimeTrackerModule(ActionEvent event) {
        loadModule(TIME_TRACKER_MODULE);
    }

    @FXML
    private void loadCreatorModule(ActionEvent event) {
        loadModule(CREATOR_MODULE);

    }

    @FXML
    private void loadOverviewModule(ActionEvent event) {
        loadModule(OVERVIEW_MODULE);
    }

}
