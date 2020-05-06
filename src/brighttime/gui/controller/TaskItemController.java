package brighttime.gui.controller;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.ITaskEntryModel;
import brighttime.gui.model.interfaces.ITaskModel;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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

    private static final String TIME_FORMAT = "HH:mm";
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

    }

    public void injectTimeTrackerController(TimeTrackerController timeTrackerController) {
        this.timeTrackerController = timeTrackerController;
    }

    public void injectModel(ITaskModel taskModel) {
        this.taskModel = taskModel;
        setTaskDetails(taskModel.getTask());
        restrictExpandButton();
    }

    public void setTaskDetails(Task task) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIME_FORMAT);

        textFieldTaskDesc.textProperty().bind(Bindings.createStringBinding(()
                -> task.getDescription(), task.descriptionProperty()));
        textFieldClient.textProperty().bind(Bindings.createStringBinding(()
                -> task.getProject().getClient().getName(), task.getProject().getClient().nameProperty()));
        textFieldProject.textProperty().bind(Bindings.createStringBinding(()
                -> task.getProject().getName(), task.getProject().nameProperty()));
        textFieldStartTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskModel.getStartTime()), taskModel.startTimeProperty()));
        textFieldEndTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskModel.getEndTime()), taskModel.startTimeProperty()));
        textFieldDuration.textProperty().bind(Bindings.createStringBinding(()
                -> taskModel.secToFormat(taskModel.calculateTaskDuration(taskModel.getDayEntryList()).toSeconds()),
                taskModel.stringDurationProperty()));

    }

    private void restrictExpandButton() {
        if (taskModel.getDayEntryList().isEmpty()) {
            btnExpandTask.setDisable(true);
            imgExpandCollapse.setImage(null);
        }
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

            TaskEntryItemController controller = fxmlLoader.getController();

            controller.injectTaskEntryModel(taskEntryModel);
            controller.setTaskEntry(taskEntry);

            vBoxTaskEntries.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handlePlayPauseTask(ActionEvent event) throws ModelException {
        if (btnPlayPause.isSelected()) {
            imgPlayPause.setImage(PAUSE_ICON_IMAGE);
            tempStartTime = LocalDateTime.now().withNano(0);
        } else {
            imgPlayPause.setImage(PLAY_ICON_IMAGE);
            tempEndTime = LocalDateTime.now().withNano(0);
            taskModel.createTaskEntry(tempStartTime, tempEndTime);
            //  refresh
            timeTrackerController.lazyRefresh();

        }
    }

}
