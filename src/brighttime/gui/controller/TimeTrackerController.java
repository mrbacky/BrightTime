/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.Map;
import java.util.ResourceBundle;
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
    
    private final String TASK_ITEM_FXML = "/brighttime/gui/view/TaskItem.fxml";
    private final String TASK_CREATOR_FXML = "/brighttime/gui/view/CreateTask.fxml";
    
    @FXML
    private VBox vBoxMain;
    private IMainModel mainModel;
    @FXML
    private StackPane spTaskCreator;
    
    private LocalDate date = LocalDate.MIN;
    private Task task = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }
    
    public void initializeView() throws ModelException {
        mainModel.loadTaskEntries();
        //mainModel.loadTasks();
        //setUpTaskCreator();
        //initTasks();
        displayTasks();
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
    
    private void displayTasks() {
        vBoxMain.getChildren().clear();
        ObservableList<TaskEntry> list = mainModel.getTaskEntryList();
        for (TaskEntry taskEntry : list) {
            LocalDate entryDate = taskEntry.getStartTime().toLocalDate();
            Task entryTask = taskEntry.getTask();
            if (!entryDate.equals(date)) {
                Label label = new Label(entryDate.toString());
                vBoxMain.getChildren().add(label);
            }
            if (entryDate.equals(date) && entryTask != task) {
                task = entryTask;
                addTaskItem(task);
            } else if (!entryDate.equals(date)) {
                task = entryTask;
                addTaskItem(task);
            }
            date = entryDate;
        }
    }
    
    private void initTasks() {
        vBoxMain.getChildren().clear();
        Map<Integer, Task> taskList = mainModel.getTasks();
        for (Map.Entry<Integer, Task> task : taskList.entrySet()) {
            addTaskItem(task.getValue());
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
    
}
