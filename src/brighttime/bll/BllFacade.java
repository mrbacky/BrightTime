/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.bll;

import brighttime.be.Task;
import java.util.List;

/**
 *
 * @author rados
 */
public interface BllFacade {

    public List<Task> loadTasks();
    
    public String convertDuration(int duration);
    
    public int convertDuration(String duration);
}
