/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.gui.model;

import brighttime.be.Task;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author rados
 */
public interface ModelFacade {

    public void loadTasks();

    public ObservableList<Task> getTasks();
    
    
}