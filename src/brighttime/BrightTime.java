package brighttime;

import brighttime.dal.dao.concretes.TaskDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author rado
 */
public class BrightTime extends Application {

    private TaskDAO t;
    @Override
    public void start(Stage stage) throws Exception {
        t = new TaskDAO();
        t.getTaskEntries();
        
        Image icon = new Image(getClass().getResourceAsStream("/brighttime/gui/view/assets/sun_48px.png"));
        stage.getIcons().add(icon);
        stage.setTitle("BrightTime");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BrightTime.class.getResource("/brighttime/gui/view/Root.fxml"));
        Parent root = loader.load();
        stage.setMinWidth(850);
        stage.setMinHeight(400);
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
