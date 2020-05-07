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

    private final StringProperty stringDuration = new SimpleStringProperty();
    private final StringProperty entryDescription = new SimpleStringProperty();
    private TaskEntry taskEntry;

    public TaskEntryModel(BllFacade bllManager) {
        this.bllManager = bllManager;
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

    @Override
    public String getEntryDescription() {
        return entryDescription.get();
    }

    @Override
    public void setEntryDescription(String value) {
        entryDescription.set(value);
    }

    @Override
    public StringProperty entryDescriptionProperty() {
        return entryDescription;
    }

    @Override
    public void setTaskEntry(TaskEntry taskEntry) {
        this.taskEntry = taskEntry;
    }

    @Override
    public TaskEntry getTaskEntry() {
        return taskEntry;
    }
}
