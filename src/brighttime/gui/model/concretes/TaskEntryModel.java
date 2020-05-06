package brighttime.gui.model.concretes;

import brighttime.be.TaskEntry;
import brighttime.bll.BllFacade;
import brighttime.gui.model.interfaces.ITaskEntryModel;
import java.time.Duration;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author rado
 */
public class TaskEntryModel implements ITaskEntryModel {

    private BllFacade bllManager;
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty stringDuration = new SimpleStringProperty();

    public TaskEntryModel(BllFacade bllManager) {
        this.bllManager = bllManager;
    }

    @Override
    public String getDescription() {
        return description.get();
    }

    @Override
    public void setDescription(String value) {
        description.set(value);
    }

    @Override
    public StringProperty descriptionProperty() {
        return description;
    }

    @Override
    public String getStringDuration() {
        return stringDuration.get();
    }

    @Override
    public void setStringDuration(String value) {
        stringDuration.set(value);
    }

    @Override
    public StringProperty stringDurationProperty() {
        return stringDuration;
    }

    @Override
    public Duration calculateDuration(TaskEntry taskEntry) {
        return bllManager.calculateDuration(taskEntry);
    }

    @Override
    public String secToFormat(long sec) {
        return bllManager.secToFormat(sec);

    }

}
