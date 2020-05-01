/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.controller;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.gui.model.ModelFacade;
import brighttime.gui.model.concretes.TaskModel;
import brighttime.gui.model.interfaces.ITaskModel;
import com.sun.source.util.TaskListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.lang.model.util.Elements;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class TimeTrackerController implements Initializable {

    private final String TASK_ITEM_FXML = "/brighttime/gui/view/TaskItem.fxml";
    @FXML
    private VBox vBoxMain;
    private ModelFacade modelManager;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    void injectModelManager(ModelFacade modelManager) {
        this.modelManager = modelManager;
    }

    public void initTasks() {
        modelManager.loadTasks();
        List<Task> taskList = modelManager.getTasks();
        for (Task task : taskList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_ITEM_FXML));
                
                Parent root = fxmlLoader.load();
                
                TaskItemController controller = fxmlLoader.getController();
                controller.setTask(task);
                
                vBoxMain.getChildren().add(root);
                
                
//                controller.setTaskTotalInterval(task);
            } catch (IOException ex) {
                Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

//    private void displayTaskEntries(Task task) {
//
//    }

//    private void initTaskEntries(List<TaskEntry> taskEntryList) {
//
//        for (TaskEntry taskEntry : taskEntryList) {
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_ENTRY_ITEM_FXML));
//                Parent root = fxmlLoader.load();
//                TaskEntryItemController controller = fxmlLoader.getController();
//                controller.setTaskEntry(taskEntry);
//                vBoxMain.getChildren().add(root);
//
//            } catch (IOException ex) {
//                Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//    }
}
