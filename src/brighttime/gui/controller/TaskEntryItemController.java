package brighttime.gui.controller;

import brighttime.be.TaskEntry;
import brighttime.gui.model.interfaces.ITaskEntryModel;
import brighttime.gui.model.interfaces.ITaskModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
    private JFXTextField textFieldStartTime;
    @FXML
    private JFXTextField textFieldEndTime;
    @FXML
    private JFXTextField textFieldDuration;
    @FXML
    private JFXButton btnRemoveTask;
    private ITaskModel taskModel;
    private ITaskEntryModel taskEntryModel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textFieldStartTime.setEditable(true);
    }

    public void injectTaskEntryModel(ITaskEntryModel taskEntryModel) {
        this.taskEntryModel = taskEntryModel;
    }

    public void setTaskEntry(TaskEntry taskEntry) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        textFieldTaskEntryDesc.textProperty().bind(Bindings.createStringBinding(()
                -> taskEntry.getDescription(), taskEntry.descriptionProperty()));
        textFieldStartTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskEntry.getStartTime()), taskEntry.endTimeProperty()));
        textFieldEndTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskEntry.getEndTime()), taskEntry.endTimeProperty()));
        textFieldDuration.textProperty().bind(Bindings.createStringBinding(()
                -> taskEntryModel.secToFormat(taskEntryModel.calculateDuration(taskEntry).toSeconds()), taskEntryModel.stringDurationProperty()));

//        textFieldDuration.setText(taskModel.secToFormat(taskModel.calculateTaskDuration(taskEntry).toSeconds()));
    }

}
