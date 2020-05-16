package brighttime;

import brighttime.gui.controller.LoginController;
import brighttime.gui.model.ModelCreator;
import brighttime.gui.model.interfaces.IAuthenticationModel;
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

    private final String LOGIN_VIEW = "/brighttime/gui/view/Login.fxml";
    private final String APP_ICON = "/brighttime/gui/view/assets/sun_48px.png";
    

    @Override
    public void start(Stage stage) throws Exception {

        Image icon = new Image(getClass().getResourceAsStream(APP_ICON));
        stage.getIcons().add(icon);
        stage.setTitle("BrightTime");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(BrightTime.class.getResource(LOGIN_VIEW));
        Parent root = fxmlLoader.load();

        IAuthenticationModel authenticationModel = ModelCreator.getInstance().createAuthenticationModel();

        LoginController controller = fxmlLoader.getController();
        controller.injectAuthenticationModel(authenticationModel);

        
        Scene scene = new Scene(root);
        stage.setResizable(false);
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
