package brighttime.gui.controller;

import brighttime.be.TaskEntry;
import brighttime.gui.model.interfaces.ITaskEntryModel;
import brighttime.gui.model.interfaces.ITaskModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class TaskEntryItemController implements Initializable {

    private static final String DATE_TIME_FORMAT = "HH:mm";

    @FXML
    private JFXTextField textFieldTaskEntryDesc;

    @FXML
    private JFXButton btnRemoveTask;
    private ITaskEntryModel taskEntryModel;
    @FXML
    private Label lblDuration;
    @FXML
    private JFXTimePicker timePickerStartTime;
    @FXML
    private JFXTimePicker timePickerEndTime;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void injectTaskEntryModel(ITaskEntryModel taskEntryModel) {
        this.taskEntryModel = taskEntryModel;
        setTaskEntryDetails(taskEntryModel.getTaskEntry());
    }

    public void setTaskEntryDetails(TaskEntry taskEntry) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        textFieldTaskEntryDesc.textProperty().bind(Bindings.createStringBinding(()
                -> taskEntry.getTask().getDescription(), taskEntryModel.entryDescriptionProperty()));

        timePickerStartTime.valueProperty().bindBidirectional(taskEntryModel.startTimeProperty());
        timePickerEndTime.valueProperty().bindBidirectional(taskEntryModel.endTimeProperty());
        
        lblDuration.textProperty().bindBidirectional(taskEntryModel.stringDurationProperty());
        
//        lblDuration.textProperty().bind(Bindings.createStringBinding(()
//                -> taskEntryModel.secToFormat(taskEntryModel.calculateDuration(taskEntry).toSeconds()), taskEntryModel.stringDurationProperty()));
    }
    
    

}
