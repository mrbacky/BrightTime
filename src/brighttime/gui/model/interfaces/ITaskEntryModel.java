/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.model.interfaces;

import brighttime.be.TaskEntry;
import java.time.Duration;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;

/**
 *
 * @author rados
 */
public interface ITaskEntryModel {

    String getStringDuration();

    void setStringDuration(String value);

    StringProperty stringDurationProperty();

    Duration calculateDuration(TaskEntry taskEntry);

    String secToFormat(long sec);

    String getEntryDescription();

    void setEntryDescription(String value);

    StringProperty entryDescriptionProperty();

    public void setTaskEntry(TaskEntry taskEntry);
    
    TaskEntry getTaskEntry();

}
