package brighttime.gui.util;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author annem
 */
public class HoverNode extends StackPane {

    public HoverNode(String description, String project, double value) {
        //setPrefSize(15, 15);

        final Label label = createLabel(description, project, value);

        setOnMouseEntered((MouseEvent mouseEvent) -> {
            getChildren().setAll(label);
            setCursor(Cursor.CROSSHAIR);
            toFront();
        });
        setOnMouseExited((MouseEvent mouseEvent) -> {
            getChildren().clear();
        });
    }

    private Label createLabel(String description, String project, double value) {
        final Label label = new Label(description + System.lineSeparator() + project + System.lineSeparator() + value + " hours");
        label.getStyleClass().add("lblHoverNode");
        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }

}
