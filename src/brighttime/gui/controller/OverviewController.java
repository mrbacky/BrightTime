package brighttime.gui.controller;

import brighttime.gui.model.ModelFacade;
import brighttime.gui.model.interfaces.IMainModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class OverviewController implements Initializable {

    private IMainModel mainModel;

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
