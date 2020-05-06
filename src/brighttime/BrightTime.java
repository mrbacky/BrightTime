package brighttime;

import brighttime.gui.controller.RootController;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.interfaces.IMainModel;
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

    @Override
    public void start(Stage stage) throws Exception {

        Image icon = new Image(getClass().getResourceAsStream("/brighttime/gui/view/assets/sun_48px.png"));
        stage.getIcons().add(icon);
        stage.setTitle("BrightTime");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BrightTime.class.getResource("/brighttime/gui/view/Root.fxml"));
        Parent root = loader.load();

        IMainModel mainModel = ModelCreator.getInstance().createMainModel();
        RootController controller = loader.getController();
        controller.injectMainModel(mainModel);
        controller.initializeView();

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
