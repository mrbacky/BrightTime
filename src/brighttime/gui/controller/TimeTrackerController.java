package brighttime.gui.controller;

import brighttime.be.Task;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IMainModel;
import brighttime.gui.model.interfaces.ITaskModel;
import brighttime.gui.util.AlertManager;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

    @FXML
    private VBox vBoxMain;
    @FXML
    private StackPane spTaskCreator;

    private final String TASK_ITEM_FXML = "/brighttime/gui/view/TaskItem.fxml";
    private final String TASK_CREATOR_FXML = "/brighttime/gui/view/CreateTask.fxml";

    private IMainModel mainModel;
    private final AlertManager alertManager;
    private LocalDate date = LocalDate.MIN;

//    private ObservableList<Node> taskItems = FXCollections.observableArrayList();
//    private ObservableMap<LocalDate, List<Node>> nodeMap = FXCollections.observableHashMap();
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

    public void initializeView() {
        try {
//            vBoxMain.getChildren().clear();

            setUpTaskCreator();
            mainModel.loadTasks();
            initTasks();
            ObservableList<Node> nodeList = mainModel.getNodeList();

            vBoxMain.getChildren().clear();
            vBoxMain.getChildren().addAll(nodeList);

            nodeList.addListener((ListChangeListener.Change<? extends Node> c) -> {
                vBoxMain.getChildren().clear();
                vBoxMain.getChildren().addAll(nodeList);
            });

//            for (Map.Entry<LocalDate, List<Node>> entry : nodeMap.entrySet()) {
//                LocalDate key = entry.getKey();
//                List<Node> value = entry.getValue();
//                vBoxMain.getChildren().addAll(value);
//                System.out.println("im in for loop in initialize");
//
//            }
//
//            nodeMap.addListener((MapChangeListener.Change<? extends LocalDate, ? extends List<Node>> change) -> {
//                mainModel.getTasks();
//                vBoxMain.getChildren().clear();
//                for (Map.Entry<LocalDate, List<Node>> entry : nodeMap.entrySet()) {
//                    LocalDate key = entry.getKey();
//                    List<Node> value = entry.getValue();
//                    vBoxMain.getChildren().addAll(value);
//                    System.out.println("im in listener");
//
//                }
//
//            });
//            taskMap.addListener((MapChangeListener.Change<? extends LocalDate, ? extends List<Task>> change) -> {
//                vBoxMain.getChildren().clear();
//                for (Map.Entry<LocalDate, List<Task>> entry : taskMap.entrySet()) {
//                    LocalDate key = entry.getKey();
//                    List<Task> value = entry.getValue();
//                    vBoxMain.getChildren().addAll(value);
//                }
//            });
        } catch (ModelException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }

//        ObservableMap<LocalDate, List<Task>> obsMap = mainModel.getTasks();
//        obsMap.addListener((MapChangeListener.Change<? extends LocalDate, ? extends List<Task>> change) -> {
//            initTasks();
//
//        });
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

    public void initTasks() {
        long start = System.currentTimeMillis();
        //            mainModel.loadTasks();
        Map<LocalDate, List<Task>> taskList = mainModel.getTasks();
        Map<LocalDate, List<Task>> orderedMap = new TreeMap<>(Collections.reverseOrder());
        orderedMap.putAll(taskList);

        for (Map.Entry<LocalDate, List<Task>> entry : orderedMap.entrySet()) {
            LocalDate dateKey = entry.getKey();
            List<Task> taskListValue = entry.getValue();
            if (!dateKey.equals(date)) {
                String formatted = dateKey.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
                Label label = new Label(formatted);
                label.getStyleClass().add("labelMenuItem");
                label.translateXProperty().set(25);
                date = dateKey;
                mainModel.getNodeList().add(label);
            }
            VBox taskVBox = new VBox();
            for (Task task : taskListValue) {
                taskVBox.getChildren().add(addTaskItem(task));
            }
            mainModel.getNodeList().add(taskVBox);

        }

        //  add node list to vbox
        System.out.println("time passed: " + (System.currentTimeMillis() - start));

    }

    public Node addTaskItem(Task task) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(TASK_ITEM_FXML));
            Parent taskFXML = fxmlLoader.load();
            ITaskModel taskModel = ModelCreator.getInstance().createTaskModel();
            taskModel.setTask(task);

            taskModel.setDate(date);

            taskModel.setTaskEntryListIfNewTask();
            TaskItemController controller = fxmlLoader.getController();
            controller.injectTimeTrackerController(this);
            controller.injectModel(taskModel);
            return taskFXML;
//            vBoxMain.getChildren().add(root);
//            List<Node> nodeList = nodeMap.get(taskModel.getDate());
//            nodeList.add(taskFXML);
//            System.out.println("im in ass Task item under add FXML to list in map");

        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //                List<Node> nodeList = nodeMap.get(dateKey);
//                if (nodeList == null) {
//                    nodeList = new ArrayList<Node>();
//                    nodeList.add(label);
//                    nodeMap.put(dateKey, nodeList);
//                } else {
//                    nodeMap.put(dateKey, nodeList);
//                }
    //                vBoxMain.getChildren().add(label);
    //  add to node list/map
}
