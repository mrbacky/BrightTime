package brighttime.gui.util;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 *
 * @author annem
 */
public class ActiveFilterButton extends StackPane {

    private final StringProperty selectedFilter = new SimpleStringProperty();
    private JFXButton btn;

    ActiveFilterButtonController controller;

    public ActiveFilterButton() {
        super();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ActiveFilterButton.fxml"));
            controller = new ActiveFilterButtonController();
            fxmlLoader.setController(controller);
            Node node = fxmlLoader.load();
            this.getChildren().add(node);
            controller.getLblActiveFilter().textProperty().bind(selectedFilter);
            setBtn(controller.getBtnRemoveFilter());
        } catch (IOException ex) {
            Logger.getLogger(ActiveFilterButton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StringProperty getSelectedFilter() {
        return selectedFilter;
    }

    public void setSelectedFilter(String value) {
        selectedFilter.set(value);
    }

    public StringProperty selectedFilterProperty() {
        return selectedFilter;
    }

    public JFXButton getBtn() {
        return btn;
    }

    private void setBtn(JFXButton btn) {
        this.btn = btn;
    }

}
