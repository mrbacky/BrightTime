package brighttime.gui.controller;

import brighttime.gui.model.ModelFacade;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class CreatorController implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;
    @FXML
    private JFXButton btnClients;
    @FXML
    private JFXButton btnProjects;
    @FXML
    private JFXButton btnUsers;

    private ModelFacade modelManager;
    private RootController rootContr;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void injectModelManager(ModelFacade modelManager) {
        this.modelManager = modelManager;
    }

    void initializeView() {
        System.out.println("in Creator page");
        centervBox();
        showAdminClientModule();
        showAdminProjectModule();
    }

    public void setContr(RootController con) {
        this.rootContr = con;
    }

    private void centervBox() {
        vBox.translateXProperty().bind((scrollPane.widthProperty().subtract(vBox.widthProperty())).divide(2));
    }

    private void showAdminClientModule() {
        btnClients.setOnAction((event) -> {
            rootContr.loadAdminClientModule();
        });
    }

    private void showAdminProjectModule() {
        btnProjects.setOnAction((event) -> {
            rootContr.loadAdminProjectModule();
        });
    }

}
