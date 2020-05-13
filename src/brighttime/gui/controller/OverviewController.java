package brighttime.gui.controller;

import brighttime.gui.model.ModelFacade;
import brighttime.gui.model.interfaces.IMainModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class OverviewController implements Initializable {

    private IMainModel mainModel;
    @FXML
    private AnchorPane apOverview;
    @FXML
    private TableView<?> tbvTasks;
    @FXML
    private TableColumn<?, ?> colTaskDesc;
    @FXML
    private TableColumn<?, ?> colProject;
    @FXML
    private TableColumn<?, ?> colHours;
    @FXML
    private JFXComboBox<?> cboUsers;
    @FXML
    private JFXComboBox<?> cboClients;
    @FXML
    private JFXComboBox<?> cboProjects;
    @FXML
    private JFXButton btnThisWeek;
    @FXML
    private JFXButton btnThisMonth;
    @FXML
    private JFXDatePicker dpStartDate;
    @FXML
    private JFXDatePicker dpEndDate;
    @FXML
    private JFXButton btnLastWeek;
    @FXML
    private JFXButton btnLastMonth;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void initializeView() {
        System.out.println("in Overview page");

    }

    public void injectMainModel(IMainModel mainModel) {
        this.mainModel = mainModel;
    }

}
