/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.controller;

import brighttime.gui.model.ModelFacade;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author rados
 */
public class OverviewController implements Initializable {

    private ModelFacade modelManager;

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

    void injectModelManager(ModelFacade modelManager) {
        this.modelManager = modelManager;
    }

}
