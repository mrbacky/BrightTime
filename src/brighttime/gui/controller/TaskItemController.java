/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.controller;

import brighttime.be.Client;
import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.gui.model.interfaces.ITaskModel;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

//    private ImageView PLAY_ICON_IMAGE = new ImageView("/brighttime/gui/view/assets/play.png");
    private final Image PLAY_ICON_IMAGE = new Image("/brighttime/gui/view/assets/play.png");
    private final Image PAUSE_ICON_IMAGE = new Image("/brighttime/gui/view/assets/pause.png");
    private final Image EXPAND_ICON_IMAGE = new Image("/brighttime/gui/view/assets/expand.png");
    private final Image COLLAPSE_ICON_IMAGE = new Image("/brighttime/gui/view/assets/collapse.png");

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
    @FXML
    private TextField textFieldProject;
    @FXML
    private TextField textFieldClient;
    @FXML
    private TextField textFieldTaskDesc;
    @FXML
    private Button btnDeleteTask;
    private ITaskModel taskModel;
    @FXML
    private ToggleButton btnPlayPause;
    @FXML
    private ImageView imgPlayPause;

    private LocalDateTime tempStartTime;
    private LocalDateTime tempEndTime;
    @FXML
    private ImageView imgExpandCollapse;
    private TimeTrackerController timeTrackerController;

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

    public void injectTimeTrackerController(TimeTrackerController timeTrackerController) {
        this.timeTrackerController = timeTrackerController;
    }

    public void injectModel(ITaskModel taskModel) {
        this.taskModel = taskModel;
        setTaskDetails(taskModel.getTask());
    }

    public void setTaskDetails(Task task) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        textFieldTaskDesc.textProperty().bind(Bindings.createStringBinding(()
                -> task.getDescription(), task.descriptionProperty()));

        textFieldClient.textProperty().bind(Bindings.createStringBinding(()
                -> task.getProject().getClient().getName(), task.getProject().getClient().nameProperty()));

        textFieldProject.textProperty().bind(Bindings.createStringBinding(()
                -> task.getProject().getName(), task.getProject().nameProperty()));

        textFieldStartTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskModel.getStartTime()), task.startTimeProperty()));

        textFieldEndTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskModel.getEndTime()), task.endTimeProperty()));

        textFieldDuration.textProperty().bind(Bindings.createStringBinding(()
                -> taskModel.secToFormat(taskModel.calculateDuration(task).toSeconds()), task.stringDurationProperty()));

//        textFieldDuration.setText(taskModel.secToFormat(taskModel.calculateDuration(task).toSeconds()));
    }

    @FXML
    private void expandTaskItem(ActionEvent event) {

        if (btnExpandTask.isSelected()) {
            imgExpandCollapse.setImage(COLLAPSE_ICON_IMAGE);
            if (!taskModel.getTask().getTaskEntryList().isEmpty()) {
                initTaskEntries();
            }
        } else {
            imgExpandCollapse.setImage(EXPAND_ICON_IMAGE);
            vBoxTaskEntries.getChildren().clear();
        }
//        if (!taskModel.getTask().getTaskEntryList().isEmpty()) {
//
//        }

    }

    public void initTaskEntries() {
        vBoxTaskEntries.getChildren().clear();
        List<TaskEntry> taskEntries = taskModel.getTask().getTaskEntryList();
        for (TaskEntry taskEntry : taskEntries) {
            addEntryItem(taskEntry);
        }
    }

    private void addEntryItem(TaskEntry taskEntry) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_ENTRY_ITEM_FXML));
            Parent root = fxmlLoader.load();
            TaskEntryItemController controller = fxmlLoader.getController();
            controller.injectTaskModel(taskModel);
            controller.setTaskEntry(taskEntry);

            vBoxTaskEntries.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handlePlayPauseTask(ActionEvent event) {
        if (btnPlayPause.isSelected()) {
            imgPlayPause.setImage(PAUSE_ICON_IMAGE);
            tempStartTime = LocalDateTime.now();
        } else {
            imgPlayPause.setImage(PLAY_ICON_IMAGE);
            tempEndTime = LocalDateTime.now();
            taskModel.createTaskEntry(tempStartTime, tempEndTime);
            //  refresh
            timeTrackerController.initializeView();

        }
    }

}
