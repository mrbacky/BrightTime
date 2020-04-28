package brighttime;

import brighttime.dal.dao.concretes.TaskDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rados
 */
public class BrightTime extends Application {
    private TaskDAO t;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/brighttime/gui/view/Root.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        t = new TaskDAO();
        t.getTasks();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
