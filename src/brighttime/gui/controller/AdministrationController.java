package brighttime.gui.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class AdministrationController implements Initializable {

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

    private RootController rootContr;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    void initializeView() {
        centerVBox();
        showAdminClientModule();
        showAdminProjectModule();
        showAdminUserModule();
    }

    public void setContr(RootController con) {
        this.rootContr = con;
    }

    private void centerVBox() {
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

    private void showAdminUserModule() {
        btnUsers.setOnAction((ActionEvent event) -> {
            rootContr.loadAdminUserModule();
        });
    }

}
