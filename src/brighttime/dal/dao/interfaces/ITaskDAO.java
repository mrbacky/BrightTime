package brighttime.dal.dao.interfaces;

import brighttime.be.Filter;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
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
     * Creates a new task in the database.
     *
     * @param task The task to be created.
     * @return The created task.
     * @throws DalException
     */
    TaskConcrete1 createTask(TaskConcrete1 task) throws DalException;

    /**
     * Gets the tasks logged by the user between two dates.
     *
     * @param filter A filter with the parameters user, start date and end date.
     * @return A map with a list of tasks (containing entries) for each date.
     * @throws DalException
     */
    Map<LocalDate, List<TaskConcrete1>> getAllTasksWithEntries(Filter filter) throws DalException;

    /**
     * Gets all the tasks for the Overview.
     *
     * @return A list of tasks.
     * @throws DalException
     */
    List<TaskConcrete2> getAllTasks() throws DalException;

    /**
     * Gets the tasks which satisfies the filter condition for the Overview.
     *
     * @param filter The filter.
     * @return A list of filtered tasks.
     * @throws DalException
     */
    List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws DalException;

}
