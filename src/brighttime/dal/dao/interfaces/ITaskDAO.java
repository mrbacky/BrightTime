package brighttime.dal.dao.interfaces;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.dal.DalException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rados
 */
public interface ITaskDAO {

    /**
     * Gets the tasks logged between today and 30 days ago.
     *
     * @return A list of tasks.
     * @throws DalException
     */
    List<TaskEntry> getTaskEntries() throws DalException;

    /**
     * Gets the tasks logged between today and 30 days ago.
     *
     * @return A map of tasks.
     * @throws DalException
     */
    Map getTasksWithTaskEntries() throws DalException;

    /**
     * Creates a new task in the database.
     *
     * @param task The task to be created.
     * @return The created task.
     * @throws DalException
     */
    Task createTask(Task task) throws DalException;

    /**
     * To be removed.
     *
     * @return
     */
    List<Task> getTasksList();

}
