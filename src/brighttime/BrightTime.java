/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author rados
 */
public class BrightTime extends Application {

    public static Stage stage = null;
    

    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BrightTime.class.getResource("/brighttime/gui/view/Root.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.isResizable();
        stage.setScene(scene);
        this.stage = stage;
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
