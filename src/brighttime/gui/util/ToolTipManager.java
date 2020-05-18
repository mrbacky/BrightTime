package brighttime.gui.util;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Tooltip;

/**
 *
 * @author annem
 */
public class ToolTipManager {

    public void setToolTipForOneButton(JFXButton button, String tip) {
        button.setTooltip(new Tooltip(tip));
    }

}
