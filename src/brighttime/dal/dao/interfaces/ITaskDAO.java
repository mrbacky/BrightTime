package brighttime.dal.dao.interfaces;

import brighttime.dal.DalException;
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
     * @throws brighttime.dal.DalException
     */
    Map getTasksWithTaskEntries() throws DalException;

}
