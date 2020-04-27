package brighttime.dal.dao.interfaces;

import brighttime.be.Task;
import java.util.List;

/**
 *
 * @author rados
 */
public interface ITaskDAO {

    /**
     * Gets the tasks created on the current day.
     *
     * @return A list of tasks.
     */
    List<Task> getTasksForCurrentDay();

}
