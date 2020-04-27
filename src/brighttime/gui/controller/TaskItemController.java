/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.controller;

import brighttime.be.Task;
import java.net.URL;
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

    @FXML
    private HBox hBoxItemElements;
    @FXML
    private TextField textFieldTaskName;
    @FXML
    private TextField textFieldTaskProjectName;
    @FXML
    private TextField textFieldTaskClientName;
    @FXML
    private TextField textFieldTaskDuration;
    @FXML
    private Button btnPlayPause;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textFieldTaskName.setEditable(false);
        textFieldTaskProjectName.setEditable(false);
        textFieldTaskClientName.setEditable(false);
        textFieldTaskDuration.setEditable(false);

    }

    void setTask(Task task) {

        textFieldTaskName.textProperty().bind(Bindings.createStringBinding(() -> task.getName(), task.nameProperty()));
        textFieldTaskProjectName.textProperty().bind(Bindings.createStringBinding(() -> task.getProjectName(), task.projectNameProperty()));
        textFieldTaskClientName.textProperty().bind(Bindings.createStringBinding(() -> task.getClientName(), task.clientNameProperty()));
        textFieldTaskDuration.textProperty().bind(Bindings.createStringBinding(() -> task.getStringDuration(), task.stringDurationProperty()));

    }

}
