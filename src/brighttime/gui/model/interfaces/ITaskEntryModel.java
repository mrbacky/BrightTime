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

    public String getStringDuration();

    public void setStringDuration(String value);

    public StringProperty stringDurationProperty();

    Duration calculateDuration(TaskEntry taskEntry);

    public String secToFormat(long sec);

}
