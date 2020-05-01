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

/**
 * FXML Controller class
 *
 * @author rados
 */
public class TaskEntryItemController implements Initializable {

    private static final String DATE_TIME_FORMAT = "HH:mm";

    @FXML
    private TextField textFieldTaskEntryDesc;
    @FXML
    private TextField textFieldStartTime;
    @FXML
    private TextField textFieldEndTime;
    @FXML
    private TextField textFieldDuration;
    @FXML
    private Button btnRemoveTask;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void setTaskEntry(TaskEntry taskEntry) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        textFieldTaskEntryDesc.textProperty().bind(Bindings.createStringBinding(()
                -> taskEntry.getDescription(), taskEntry.descriptionProperty()));
        textFieldStartTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskEntry.getStartTime()), taskEntry.endTimeProperty()));
        textFieldEndTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskEntry.getEndTime()), taskEntry.endTimeProperty()));
//        textFieldDuration.textProperty().bind(Bindings.createStringBinding(()
//                -> taskEntry.getStringDuration(taskEntry.getStartTime(), taskEntry.getEndTime()), taskEntry.stringDurationProperty()));

    }

}
