/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.controller;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class TaskItemController implements Initializable {

    private static final String DATE_TIME_FORMAT = "HH:mm";

    @FXML
    private HBox hBoxItemElements;
    @FXML
    private TextField textFieldTaskName;
    @FXML
    private TextField textFieldTaskProjectName;
    @FXML
    private TextField textFieldTaskClientName;
    @FXML
    private Button btnPlayPause;
    @FXML
    private TextField textFieldStartTime;
    @FXML
    private TextField textFieldEndTime;
    @FXML
    private TextField textFieldDuration;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textFieldTaskName.setEditable(false);
        textFieldTaskProjectName.setEditable(false);
        textFieldTaskClientName.setEditable(false);
        textFieldDuration.setEditable(false);
        textFieldStartTime.setEditable(false);
        textFieldEndTime.setEditable(false);
    }

    void setTask(Task task) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        textFieldTaskName.textProperty().bind(Bindings.createStringBinding(()
                -> task.getDescription(), task.descriptionProperty()));
        textFieldTaskClientName.textProperty().bind(Bindings.createStringBinding(()
                -> task.getProject().getClient().getName(), task.getProject().getClient().nameProperty()));
        textFieldTaskProjectName.textProperty().bind(Bindings.createStringBinding(()
                -> task.getProject().getName(), task.getProject().nameProperty()));
        textFieldStartTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(getTaskStartTime(task)), task.startTimeProperty()));
        textFieldEndTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(getTaskEndTime(task)), task.endTimeProperty()));

    }

    private LocalDateTime getTaskStartTime(Task task) {
        LocalDateTime taskStartTime = task.getTaskEntryList().get(0).getStartTime();
        return taskStartTime;
    }

    private LocalDateTime getTaskEndTime(Task task) {
        TaskEntry latestTaskEntry = (task.getTaskEntryList()).get(task.getTaskEntryList().size() - 1);
        LocalDateTime taskEndTime = latestTaskEntry.getEndTime();
        return taskEndTime;
    }

    void setTaskTotalInterval(Task task) {
        textFieldDuration.textProperty().bind(Bindings.createStringBinding(()
                -> task.getStringDuration(task.getTaskEntryList()), task.stringDurationProperty()));

    }

}
