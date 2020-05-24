package brighttime.dal.dao.interfaces;

import brighttime.be.Filter;
import brighttime.be.TaskConcrete1;
import brighttime.be.TaskConcrete2;
import brighttime.be.User;
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
     * Gets the tasks logged between today and 30 days ago of the user.
     *
     * @param user The user.
     * @return A map with a list of tasks (containing entries) for each day.
     * @throws DalException
     */
    Map<LocalDate, List<TaskConcrete1>> getAllTasksWithEntries(User user) throws DalException;

    List<TaskConcrete2> getAllTasks() throws DalException;

    List<TaskConcrete2> getAllTasksFiltered(Filter filter) throws DalException;

    boolean hasTask(User user) throws DalException;

}
