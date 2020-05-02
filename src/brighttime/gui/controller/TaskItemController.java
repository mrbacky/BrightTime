/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class TaskItemController implements Initializable {

    private static final String DATE_TIME_FORMAT = "HH:mm";
    private final String TASK_ENTRY_ITEM_FXML = "/brighttime/gui/view/TaskEntryItem.fxml";

    @FXML
    private TextField textFieldStartTime;
    @FXML
    private TextField textFieldEndTime;
    @FXML
    private TextField textFieldDuration;
    @FXML
    private ToggleButton btnExpandTask;
    @FXML
    private VBox vBoxTaskEntries;
    private Task task;
    @FXML
    private TextField textFieldProject;
    @FXML
    private TextField textFieldClient;
    @FXML
    private TextField textFieldTaskDesc;
    @FXML
    private Button btnPlayTask;
    @FXML
    private Button btnDeleteTask;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /*
        
        task Helper class (util package)
        Create task model
        move all of the task dato from here to task Model
        inject task model to this contr instead of setTask method
            alt.    implement factory for injecting the model
        move all of the logic for calculating duration from BEs to BLL
        access this functionality through contr - model - bll
        
        what about main model ???
        
        
        
        
        
        
         */
    }

    public void setTask(Task task) {
        this.task = task;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        textFieldTaskDesc.textProperty().bind(Bindings.createStringBinding(()
                -> task.getDescription(), task.descriptionProperty()));

        textFieldClient.textProperty().bind(Bindings.createStringBinding(()
                -> task.getProject().getClient().getName(), task.getProject().getClient().nameProperty()));

        textFieldProject.textProperty().bind(Bindings.createStringBinding(()
                -> task.getProject().getName(), task.getProject().nameProperty()));

//        textFieldStartTime.textProperty().bind(Bindings.createStringBinding(()
//                -> dtf.format(task.getTaskStartTime()), task.startTimeProperty()));
//
//        textFieldEndTime.textProperty().bind(Bindings.createStringBinding(()
//                -> dtf.format(task.getTaskEndTime()), task.endTimeProperty()));

//        textFieldDuration.textProperty().bind(Bindings.createStringBinding(()
//                -> task.getStringDuration(), task.stringDurationProperty()));
    }

    @FXML
    private void expandTaskItem(ActionEvent event) {
        
        if (btnExpandTask.isSelected()) {
            initTaskEntries();
        } else {
            vBoxTaskEntries.getChildren().clear();
        }
    }

    public void initTaskEntries() {

        List<TaskEntry> taskEntries = task.getTaskEntryList();
        for (TaskEntry taskEntry : taskEntries) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_ENTRY_ITEM_FXML));
                Parent root = fxmlLoader.load();
                TaskEntryItemController controller = fxmlLoader.getController();
                controller.setTaskEntry(taskEntry);
                vBoxTaskEntries.getChildren().add(root);
            } catch (IOException ex) {
                Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
