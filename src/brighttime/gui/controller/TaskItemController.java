package brighttime.gui.controller;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.ITaskEntryModel;
import brighttime.gui.model.interfaces.ITaskModel;
import brighttime.gui.util.AlertManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private ToggleButton btnExpandTask;
    @FXML
    private VBox vBoxTaskEntries;
    @FXML
    private JFXTextField textFieldProject;
    @FXML
    private JFXTextField textFieldClient;
    @FXML
    private JFXTextField textFieldTaskDesc;
    @FXML
    private JFXButton btnDeleteTask;
    @FXML
    private ToggleButton btnPlayPause;
    @FXML
    private ImageView imgPlayPause;
    @FXML
    private ImageView imgExpandCollapse;

    private TimeTrackerController timeTrackerController;
    private ITaskModel taskModel;
    private final AlertManager alertManager;
    private LocalDateTime tempStartTime;
    private LocalDateTime tempEndTime;
    @FXML
    private Label lblEndTime;
    @FXML
    private Label lblStartTime;
    @FXML
    private Label lblDuration;

    public TaskItemController() {
        this.alertManager = new AlertManager();
    }

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
        if (taskModel.getDayEntryList().isEmpty()) {
            btnExpandTask.setDisable(true);
            imgExpandCollapse.setImage(null);
        }
    }

    public void setTaskDetails(Task task) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        textFieldTaskDesc.textProperty().bind(Bindings.createStringBinding(()
                -> task.getDescription(), task.descriptionProperty()));

        textFieldClient.textProperty().bind(Bindings.createStringBinding(()
                -> task.getProject().getClient().getName(), task.getProject().getClient().nameProperty()));

        textFieldProject.textProperty().bind(Bindings.createStringBinding(()
                -> task.getProject().getName(), task.getProject().nameProperty()));

        lblStartTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskModel.getStartTime()), taskModel.startTimeProperty()));

        lblEndTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskModel.getEndTime()), taskModel.startTimeProperty()));

        lblDuration.textProperty().bind(Bindings.createStringBinding(()
                -> taskModel.secToFormat(taskModel.calculateTaskDuration(taskModel.getDayEntryList()).toSeconds()), taskModel.stringDurationProperty()));

//        textFieldDuration.setText(taskModel.secToFormat(taskModel.calculateTaskDuration(task).toSeconds()));
    }

    @FXML
    private void expandTaskItem(ActionEvent event) {

        if (btnExpandTask.isSelected()) {
            imgExpandCollapse.setImage(COLLAPSE_ICON_IMAGE);
            if (!taskModel.getDayEntryList().isEmpty()) {
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
        List<TaskEntry> dayEntries = taskModel.getDayEntryList();
        for (TaskEntry entry : dayEntries) {
            addEntryItem(entry);
        }
    }

    private void addEntryItem(TaskEntry taskEntry) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_ENTRY_ITEM_FXML));
            Parent root = fxmlLoader.load();

            ITaskEntryModel taskEntryModel = ModelCreator.getInstance().createTaskEntryModel();
            taskEntryModel.setTaskEntry(taskEntry);
            taskEntryModel.setStartTime(taskEntry.getStartTime().toLocalTime());
            taskEntryModel.setEndTime(taskEntry.getEndTime().toLocalTime());
            taskEntryModel.setStringDuration(taskEntryModel.secToFormat(taskEntryModel.calculateDuration(taskEntry).toSeconds()));

            TaskEntryItemController controller = fxmlLoader.getController();
            controller.injectTaskEntryModel(taskEntryModel);
            vBoxTaskEntries.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handlePlayPauseTask(ActionEvent event) {
        if (btnPlayPause.isSelected()) {
            imgPlayPause.setImage(PAUSE_ICON_IMAGE);
            LocalDate date = LocalDate.now();
            LocalTime startTime = LocalTime.now();

            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);

            tempStartTime = LocalDateTime.now().withNano(0);
        } else {
            try {
                imgPlayPause.setImage(PLAY_ICON_IMAGE);
                tempEndTime = LocalDateTime.now().withNano(0);
                taskModel.addTaskEntry(tempStartTime, tempEndTime);
                //  refresh
                timeTrackerController.initializeView();
            } catch (ModelException ex) {
                alertManager.showAlert("Could not store the logged entry.", "An error occured: " + ex.getMessage());
            }

        }
    }

}
