package brighttime;

import brighttime.be.Client;
import brighttime.dal.dao.concretes.ProjectDAO;
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
    private ProjectDAO p;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/brighttime/gui/view/Root.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        p = new ProjectDAO();
        p.getProjects(new Client(2,"sth"));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
