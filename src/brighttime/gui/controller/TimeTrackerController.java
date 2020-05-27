package brighttime.gui.controller;

import brighttime.be.TaskConcrete1;
import brighttime.be.User;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.model.interfaces.ITaskModel;
import brighttime.gui.util.AlertManager;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class TimeTrackerController implements Initializable {

    @FXML
    private VBox vBoxMain;
    private VBox vBoxCreator;
    @FXML
    private JFXButton btnSwitchMode;

    private final String TASK_ITEM_FXML = "/brighttime/gui/view/TaskItem.fxml";
    private final String TASK_CREATOR_FXML = "/brighttime/gui/view/CreateTask.fxml";

    private LocalDate taskFilterStartDate;
    private LocalDate taskFilterEndDate;

    private CreateTaskController createTaskContr;
    private IMainModel mainModel;
    private final AlertManager alertManager;
    private LocalDate date = LocalDate.MIN;
    private User user;
    int i = 0;
    @FXML
    private GridPane grid;
    @FXML
    private JFXDatePicker datePickerStart;
    @FXML
    private JFXDatePicker datePickerEnd;

    private final StringConverter<LocalDate> dateConverter = new LocalDateStringConverter(FormatStyle.FULL, Locale.ENGLISH, Chronology.ofLocale(Locale.ENGLISH));

    public TimeTrackerController() {
        this.alertManager = new AlertManager();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

    private void setUser() {
        user = mainModel.getUser();
    }

    private MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener;

//    @Override
//    protected void finalize() throws Throwable {
//        mainModel.getTasks().removeListener(taskMapListener);
//        System.out.println("Object is destroyed by the Garbage Collector");
//    }
    public void initializeView() {
        try {
            setInitialFilter();
            setUser();
            setUpTaskMapListener();
            setUpTaskCreator();

            mainModel.loadTasks(user, datePickerStart.getValue(), datePickerEnd.getValue());
            initTasks();

            switchLoggingMode();

        } catch (ModelException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setUpTaskMapListener() {
        taskMapListener = (MapChangeListener.Change<? extends LocalDate, ? extends List<TaskConcrete1>> change) -> {
            initTasks();
            System.out.println("init tasks");
        };
        mainModel.addTaskMapListener(taskMapListener);

    }

    private void setUpTaskCreator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_CREATOR_FXML));

            Parent root = fxmlLoader.load();
            CreateTaskController controller = fxmlLoader.getController();
            controller.injectTimeTrackerController(this);
            controller.injectMainModel(mainModel);
            controller.initializeView();
            grid.add(root, 0, 0);

        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initTasks() {
        vBoxMain.getChildren().clear();
        Map<LocalDate, List<TaskConcrete1>> taskList = mainModel.getTasks();
        Map<LocalDate, List<TaskConcrete1>> orderedMap = new TreeMap<>(Collections.reverseOrder());

        orderedMap.putAll(taskList);
        for (Map.Entry<LocalDate, List<TaskConcrete1>> entry : orderedMap.entrySet()) {
            LocalDate dateKey = entry.getKey();
            List<TaskConcrete1> taskListValue = entry.getValue();

            if (!dateKey.equals(date)) {

                String formatted = dateKey.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                Label label = new Label(formatted);
                label.getStyleClass().add("labelMenuItem");
                vBoxMain.getChildren().add(label);
                label.translateXProperty().set(25);
                date = dateKey;
            }
            for (TaskConcrete1 task : taskListValue) {

                addTaskItem(task);
            }
        }

    }

    private void addTaskItem(TaskConcrete1 task) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_ITEM_FXML));
            Parent root = fxmlLoader.load();
            ITaskModel taskModel = ModelCreator.getInstance().createTaskModel();
            taskModel.setTask(task);
            taskModel.setDate(date);
            taskModel.initializeTaskModel();
            TaskItemController controller = fxmlLoader.getController();
            controller.injectTimeTrackerController(this);
            controller.injectModel(taskModel);
            controller.initializeView();
            vBoxMain.getChildren().add(root);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void injectCreateTaskController(CreateTaskController contr) {
        this.createTaskContr = contr;
    }

    private void switchLoggingMode() {
        btnSwitchMode.setText("Switch to manual logging");
        btnSwitchMode.getStyleClass().add("buttonSwitchMode");
        btnSwitchMode.setOnAction((event) -> {
            if (i % 2 == 0) {
                createTaskContr.manualMode();
                btnSwitchMode.setText("Switch to timer logging");
            } else if (i % 2 == 1) {
                createTaskContr.normalMode();
                btnSwitchMode.setText("Switch to manual logging");
            }
            i++;
        });

    }

    @FXML
    private void handleFilterTasksStartDate(Event event) {
        try {
            mainModel.loadTasks(user, datePickerStart.getValue(), datePickerEnd.getValue());
            initTasks();
        } catch (ModelException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleFilterTasksEndDate(Event event) {
        try {
            mainModel.loadTasks(user, datePickerStart.getValue(), datePickerEnd.getValue());
            initTasks();
        } catch (ModelException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setInitialFilter() {
        taskFilterStartDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        taskFilterEndDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        datePickerStart.setValue(taskFilterStartDate);
        datePickerEnd.setValue(taskFilterEndDate);
        datePickerStart.converterProperty().setValue(dateConverter);
        datePickerEnd.converterProperty().setValue(dateConverter);
    }

}
