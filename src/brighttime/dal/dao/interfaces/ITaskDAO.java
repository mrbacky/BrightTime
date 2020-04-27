package brighttime.dal.dao.interfaces;

import brighttime.be.Task;
import brighttime.dal.DalException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author rados
 */
public interface ITaskDAO {

    /**
     * Gets the tasks created on the current day.
     *
     * @param date The current date.
     * @return A list of tasks.
     * @throws brighttime.dal.DalException
     */
    List<Task> getTasksForCurrentDay(LocalDate date) throws DalException;

}
