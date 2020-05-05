package brighttime.gui.controller;

import brighttime.be.TaskEntry;
import brighttime.gui.model.interfaces.ITaskModel;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
    private ITaskModel taskModel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textFieldStartTime.setEditable(true);
    }

    void injectTaskModel(ITaskModel taskModel) {
        this.taskModel = taskModel;
    }

    void setTaskEntry(TaskEntry taskEntry) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        textFieldTaskEntryDesc.textProperty().bind(Bindings.createStringBinding(()
                -> taskEntry.getDescription(), taskEntry.descriptionProperty()));
        textFieldStartTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskEntry.getStartTime()), taskEntry.endTimeProperty()));
        textFieldEndTime.textProperty().bind(Bindings.createStringBinding(()
                -> dtf.format(taskEntry.getEndTime()), taskEntry.endTimeProperty()));
        textFieldDuration.textProperty().bind(Bindings.createStringBinding(()
                -> taskModel.secToFormat(taskModel.calculateDuration(taskEntry).toSeconds()), taskEntry.stringDurationProperty()));

//        textFieldDuration.setText(taskModel.secToFormat(taskModel.calculateDuration(taskEntry).toSeconds()));
    }

}
