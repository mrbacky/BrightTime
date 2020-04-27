/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.model.interfaces;

import brighttime.be.Task;
import javafx.collections.ObservableList;

/**
 *
 * @author rados
 */
public interface ITaskModel {

    public void loadTasks();

    public ObservableList<Task> getTasks();

}
