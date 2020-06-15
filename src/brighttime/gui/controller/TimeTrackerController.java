package brighttime.gui.controller;

import brighttime.be.Filter;
import brighttime.be.TaskConcrete1;
import brighttime.be.User;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.model.interfaces.ITaskModel;
import brighttime.gui.util.AlertManager;
import brighttime.gui.util.DatePickerCustomizer;
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
import javafx.collections.MapChangeListener;
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

    private final String TASK_CREATOR_FXML = "/brighttime/gui/view/TaskCreator.fxml";
    private final String TASK_ITEM_FXML = "/brighttime/gui/view/TaskItem.fxml";
    private final StringConverter<LocalDate> dateConverter = new LocalDateStringConverter(FormatStyle.FULL, Locale.ENGLISH, Chronology.ofLocale(Locale.ENGLISH));

    @FXML
    private GridPane grid;

    @FXML
    private JFXButton btnSwitchMode;

    @FXML
    private JFXDatePicker datePickerStart;
    @FXML
    private JFXDatePicker datePickerEnd;

    @FXML
    private VBox vBoxTasks;

    private User user;
    private IMainModel mainModel;
    private final AlertManager alertManager;

    private TaskCreatorController createTaskContr;

    private final DatePickerCustomizer datePickerCustomizer;
    private LocalDate taskFilterStartDate;
    private LocalDate taskFilterEndDate;

    private MapChangeListener<LocalDate, List<TaskConcrete1>> taskMapListener;

    private int i = 0;

    public TimeTrackerController() {
        this.alertManager = new AlertManager();
        this.datePickerCustomizer = new DatePickerCustomizer();
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

    public void injectCreateTaskController(TaskCreatorController contr) {
        this.createTaskContr = contr;
    }

    public void initializeView() {
        setUser();
        setDateRestrictions();
        setInitialFilter();
        listenStartDate();
        listenEndDate();
        setUpTaskMapListener();
        setUpTaskCreator();
        loadAndInitTasks();
        switchLoggingMode();
    }

    public void setUser() {
        user = mainModel.getUser();
    }

    private void setDateRestrictions() {
        datePickerCustomizer.disableFutureDates(datePickerStart);
        datePickerCustomizer.changeWrittenFutureDateToCurrentDate(datePickerStart);
        datePickerCustomizer.disableFutureDates(datePickerEnd);
        datePickerCustomizer.changeWrittenFutureDateToCurrentDate(datePickerEnd);
    }

    public void setInitialFilter() {
        taskFilterStartDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        taskFilterEndDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        datePickerStart.setValue(taskFilterStartDate);
        datePickerEnd.setValue(taskFilterEndDate);
        datePickerStart.converterProperty().setValue(dateConverter);
        datePickerEnd.converterProperty().setValue(dateConverter);
    }

    private void listenStartDate() {
        datePickerStart.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && datePickerEnd.getValue() != null) {
                filterByTimeFrame();
            }
        });
    }

    private void listenEndDate() {
        datePickerEnd.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && datePickerStart.getValue() != null) {
                filterByTimeFrame();
            }
        });
    }

    private void filterByTimeFrame() {
        if (checkTimeFrame()) {
            loadAndInitTasks();
        } else {
            alertManager.showAlert("The time frame is invalid.", "The end date is before the start date. Please check the selection.");
        }
    }

    private boolean checkTimeFrame() {
        return datePickerStart.getValue().isBefore(datePickerEnd.getValue()) || datePickerStart.getValue().isEqual(datePickerEnd.getValue());
    }

    public void loadAndInitTasks() {
        try {
            mainModel.loadTasks(new Filter(user, null, datePickerStart.getValue(), datePickerEnd.getValue()));
            initTasks();
        } catch (ModelException ex) {
            alertManager.showAlert("Unable to load tasks.", "Error: " + ex.getMessage());
        }
    }

    public void initTasks() {
        vBoxTasks.getChildren().clear();
        Map<LocalDate, List<TaskConcrete1>> taskList = mainModel.getTaskMap();
        Map<LocalDate, List<TaskConcrete1>> orderedMap = new TreeMap<>(Collections.reverseOrder());
        orderedMap.putAll(taskList);
        for (Map.Entry<LocalDate, List<TaskConcrete1>> entry : orderedMap.entrySet()) {
            LocalDate dateKey = entry.getKey();
            List<TaskConcrete1> taskListValue = entry.getValue();

            String formatted = dateKey.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            Label label = new Label(formatted);
            label.getStyleClass().add("labelMenuItem");
            vBoxTasks.getChildren().add(label);
            label.translateXProperty().set(25);
            for (TaskConcrete1 task : taskListValue) {
                addTaskItem(task, dateKey);
            }
        }
    }

    public void addTaskItem(TaskConcrete1 task, LocalDate dateKey) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_ITEM_FXML));
            Parent root = fxmlLoader.load();
            ITaskModel taskModel = ModelCreator.getInstance().createTaskModel();
            taskModel.setTask(task);
            taskModel.setDate(dateKey);
            taskModel.initializeTaskModel();
            TaskItemController controller = fxmlLoader.getController();
            controller.injectTimeTrackerController(this);
            controller.injectModel(taskModel);
            controller.initializeView();
            vBoxTasks.getChildren().add(root);
        } catch (IOException ex) {
            alertManager.showAlert("Could not create a task", "Error: " + ex.getMessage());
        }
    }

    public void setUpTaskMapListener() {
        taskMapListener = (MapChangeListener.Change<? extends LocalDate, ? extends List<TaskConcrete1>> change) -> {
            initTasks();
        };
        mainModel.addTaskMapListener(taskMapListener);

    }

    public void setUpTaskCreator() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_CREATOR_FXML));

            Parent root = fxmlLoader.load();
            TaskCreatorController controller = fxmlLoader.getController();
            controller.injectTimeTrackerController(this);
            controller.injectMainModel(mainModel);
            controller.initializeView();
            grid.add(root, 0, 0);

        } catch (IOException ex) {
            alertManager.showAlert("Could not set up task creator panel", "Error: " + ex.getMessage());
        }
    }

    public void switchLoggingMode() {
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

}
