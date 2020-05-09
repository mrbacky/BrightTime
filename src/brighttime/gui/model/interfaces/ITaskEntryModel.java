/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.model.interfaces;

import brighttime.be.TaskEntry;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
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

    LocalTime getStartTime();

    void setStartTime(LocalTime value);

    ObjectProperty startTimeProperty();

    LocalDateTime convertLTandLDtoLDT(LocalTime time, LocalDate date);

    ObjectProperty dateProperty();

    void setDate(LocalDate value);

    LocalDate getDate();

    LocalTime getEndTime();

    void setEndTime(LocalTime value);

    ObjectProperty endTimeProperty();

    void updateStartTimeListener();

    void updateEndTimeListener();

    void durationListener();

}
