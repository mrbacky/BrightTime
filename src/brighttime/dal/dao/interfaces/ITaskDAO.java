package brighttime.dal.dao.interfaces;

import brighttime.be.Task;
import brighttime.dal.DalException;
import java.util.List;

/**
 *
 * @author rados
 */
public interface ITaskDAO {

    /**
     * Gets the tasks logged between today and 30 days ago.
     *
     * @return A list of tasks.
     * @throws brighttime.dal.DalException
     */
    List<Task> getTasks() throws DalException;

}
