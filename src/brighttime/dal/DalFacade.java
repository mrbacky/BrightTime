/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brighttime.dal;

import brighttime.be.Task;
import java.util.List;

/**
 *
 * @author rados
 */
public interface DalFacade {

    /**
     *
     * @return
     */
    public List<Task> loadTasks();
    
}
