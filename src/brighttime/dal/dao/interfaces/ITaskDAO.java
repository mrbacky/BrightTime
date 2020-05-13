package brighttime.dal.dao.interfaces;

import brighttime.be.Filter;
import brighttime.be.Task;
import brighttime.be.TaskType1;
import brighttime.be.TaskType2;
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
    TaskType1 createTask(TaskType1 task) throws DalException;

    /**
     * Gets the tasks logged between today and 30 days ago.
     *
     * @return A map with a list of tasks (containing entries) for each day.
     * @throws DalException
     */
    Map<LocalDate, List<TaskType1>> Tasks() throws DalException;

    List<TaskType2> getAllTasks() throws DalException;

    List<TaskType2> getAllTasksFiltered(Filter filter) throws DalException;

}
