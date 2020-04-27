package brighttime.dal.dao.interfaces;

import brighttime.be.Task;
import java.util.List;

/**
 *
 * @author rados
 */
public interface ITaskDAO {

    List<Task> getTasksForCurrentDay();

}
