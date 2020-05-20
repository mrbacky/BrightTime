package brighttime.gui.util;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

/**
 *
 * @author annem
 */
public class ToolTipManager {

    public void setToolTipForOneButton(JFXButton button, String tip) {
        button.setTooltip(new Tooltip(tip));
    }

    public void setToolTipForOneActiveFilterButton(ActiveFilterButton button, String tip) {
        Tooltip tp = new Tooltip(tip);
        tp.setShowDelay(Duration.seconds(0));
        Tooltip.install(button, tp);
    }

}
