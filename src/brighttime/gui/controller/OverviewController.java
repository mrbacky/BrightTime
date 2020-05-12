package brighttime.gui.controller;

import brighttime.gui.model.ModelFacade;
import brighttime.gui.model.interfaces.IMainModel;
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
