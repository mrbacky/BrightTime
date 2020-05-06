package brighttime.gui.controller;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.model.interfaces.ITaskModel;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class TimeTrackerController implements Initializable {

    // lazy refresh - bad approach 
    private final String TIME_TRACKER_MODULE = "/brighttime/gui/view/TimeTracker.fxml";

    private final String TASK_ITEM_FXML = "/brighttime/gui/view/TaskItem.fxml";
    private final String TASK_CREATOR_FXML = "/brighttime/gui/view/CreateTask.fxml";

    @FXML
    private VBox vBoxMain;
    private IMainModel mainModel;
    @FXML
    private StackPane spTaskCreator;

    private LocalDate date = LocalDate.MIN;
    private Task task = null;
    private RootController rootController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    void injectRootController(RootController rootController) {
        this.rootController = rootController;
    }

    public void initializeView() throws ModelException {
        mainModel.loadTasks();
        setUpTaskCreator();
        initTasks();
    }

    private void setUpTaskCreator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_CREATOR_FXML));
            Parent root = fxmlLoader.load();
            CreateTaskController controller = fxmlLoader.getController();
            controller.injectTimeTrackerController(this);
            controller.injectMainModel(mainModel);
            controller.initializeView();
            spTaskCreator.getChildren().add(root);

        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initTasks() {
        vBoxMain.getChildren().clear();
        Map<LocalDate, List<Task>> taskList = mainModel.getTasks();
        Map<LocalDate, List<Task>> orderedMap = new TreeMap<>(Collections.reverseOrder());
        orderedMap.putAll(taskList);
        for (Map.Entry<LocalDate, List<Task>> entry : orderedMap.entrySet()) {
            LocalDate dateKey = entry.getKey();
            List<Task> taskListValue = entry.getValue();
            if (!dateKey.equals(date)) {
                Label label = new Label(dateKey.toString());
                vBoxMain.getChildren().add(label);
                date = dateKey;
            }
            for (Task task : taskListValue) {
                addTaskItem(task);
            }
        }
    }

    private void addTaskItem(Task task) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_ITEM_FXML));
            Parent root = fxmlLoader.load();
            ITaskModel taskModel = ModelCreator.getInstance().createTaskModel();
            taskModel.setTask(task);
            taskModel.setDate(date);
            TaskItemController controller = fxmlLoader.getController();
            controller.injectTimeTrackerController(this);
            controller.injectModel(taskModel);
            vBoxMain.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void lazyRefresh() {
        rootController.loadModule(TIME_TRACKER_MODULE);
    }

}
