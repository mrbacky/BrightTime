package brighttime.gui.controller;

import brighttime.be.TaskEntry;
import brighttime.be.TaskConcrete1;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.ITaskEntryModel;
import brighttime.gui.model.interfaces.ITaskModel;
import brighttime.gui.util.AlertManager;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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

    private final String DATE_TIME_FORMAT = "HH:mm";
    private final String TASK_ENTRY_ITEM_FXML = "/brighttime/gui/view/TaskEntryItem.fxml";
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
    private ToggleButton btnPlayPause;
    @FXML
    private ImageView imgPlayPause;
    @FXML
    private ImageView imgExpandCollapse;
    @FXML
    private Label lblEndTime;
    @FXML
    private Label lblStartTime;
    @FXML
    private Label lblDuration;
    @FXML
    private ImageView imgMoneyBag;

    private final AlertManager alertManager;

    private LocalDateTime tempStartTime;
    private LocalDateTime tempEndTime;
    private TimeTrackerController timeTracker;
    private ITaskModel taskModel;

    public TaskItemController() {
        this.alertManager = new AlertManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    void injectTimeTrackerController(TimeTrackerController timeTracker) {
        this.timeTracker = timeTracker;
    }

    public void injectModel(ITaskModel taskModel) {
        this.taskModel = taskModel;

    }

    void initializeView() {
        setTaskDetails(taskModel.getTask());
        setEmptyTaskStyle();
        setNonBillableTaskStyle();
    }

    private void setEmptyTaskStyle() {
        if (taskModel.getObsEntries().isEmpty()) {
            btnExpandTask.setDisable(true);
            imgExpandCollapse.setImage(null);
        }
    }

    private void setNonBillableTaskStyle() {
        if (taskModel.getTask().getBillability() == TaskConcrete1.Billability.NON_BILLABLE) {
            imgMoneyBag.setVisible(false);
        }
    }

    public void setTaskDetails(TaskConcrete1 task) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        textFieldTaskDesc.setText(task.getDescription());
        textFieldClient.setText(task.getProject().getClient().getName());
        textFieldProject.setText(task.getProject().getName());
        lblStartTime.textProperty().bindBidirectional(taskModel.startTimeProperty(), dtf.toFormat());
        lblEndTime.textProperty().bindBidirectional(taskModel.endTimeProperty(), dtf.toFormat());
        lblDuration.textProperty().bind(taskModel.stringDurationProperty());

    }

    @FXML
    private void handleExpandAndCollapseTask(ActionEvent event) {

        if (btnExpandTask.isSelected()) {
            showTaskEntries();
        } else {
            hideTaskEntries();
        }

    }

    private void showTaskEntries() {
        imgExpandCollapse.setImage(COLLAPSE_ICON_IMAGE);
        btnExpandTask.setSelected(true);
        if (!taskModel.getObsEntries().isEmpty()) {
            initTaskEntries();
        }
    }

    private void hideTaskEntries() {
        imgExpandCollapse.setImage(EXPAND_ICON_IMAGE);
        vBoxTaskEntries.getChildren().clear();
    }

    public void initTaskEntries() {
        vBoxTaskEntries.getChildren().clear();
        List<TaskEntry> dayEntries = taskModel.getObsEntries();
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
            taskEntryModel.initializeTaskEntryModel();
            TaskEntryItemController controller = fxmlLoader.getController();
            controller.injectTaskEntryModel(taskEntryModel);
            controller.initializeView();
            vBoxTaskEntries.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handlePlayPauseTask(ActionEvent event) {
        if (btnPlayPause.isSelected()) {
            playTask();
        } else {
            pauseTask();
        }
    }

    private void playTask() {
        imgPlayPause.setImage(PAUSE_ICON_IMAGE);
        tempStartTime = LocalDateTime.now().withNano(0);
    }

    private void pauseTask() {
        try {
            imgPlayPause.setImage(PLAY_ICON_IMAGE);
            tempEndTime = LocalDateTime.now().withNano(0);
            taskModel.addTaskEntry(tempStartTime, tempEndTime);
            //  if this task is from the past ,reload everything so the new empty task will be created for current day..
            if (taskModel.getDate().equals(LocalDate.now())) {
                //  auto expand
                showTaskEntries();
            } else {//  workaround
                timeTracker.initializeView();
            }
        } catch (ModelException ex) {
            alertManager.showAlert("Could not store the logged entry.", "An error occured: " + ex.getMessage());
        }

    }

}
