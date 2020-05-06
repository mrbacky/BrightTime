package brighttime.dal.dao.interfaces;

import brighttime.be.Task;
import brighttime.be.TaskEntry;
import brighttime.dal.DalException;
import java.time.LocalDate;
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

    Map<LocalDate, List<Task>> Tasks() throws DalException;

    /**
     * Creates a new task in the database.
     *
     * @param task The task to be created.
     * @return The created task.
     * @throws DalException
     */
    Task createTask(Task task) throws DalException;

}
