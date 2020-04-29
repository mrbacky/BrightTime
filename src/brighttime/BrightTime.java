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
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author rados
 */
public class BrightTime extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Image icon = new Image(getClass().getResourceAsStream("/brighttime/gui/view/assets/sun_48px.png"));
        stage.getIcons().add(icon);
        stage.setTitle("BrightTime");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BrightTime.class.getResource("/brighttime/gui/view/Root.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
