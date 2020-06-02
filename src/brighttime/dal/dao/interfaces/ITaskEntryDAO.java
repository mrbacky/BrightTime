package brighttime.dal.dao.interfaces;

import brighttime.be.TaskEntry;
import brighttime.dal.DalException;

/**
 *
 * @author annem
 */
public interface ITaskEntryDAO {

    /**
     * Creates a task entry in the database.
     *
     * @param taskEntry The task entry to be created.
     * @return The created task entry.
     * @throws DalException
     */
    TaskEntry createTaskEntry(TaskEntry taskEntry) throws DalException;

    /**
     * Updates the start time of a task entry.
     *
     * @param taskEntry
     * @return The updated task entry.
     * @throws DalException
     */
    TaskEntry updateTaskEntryStartTime(TaskEntry taskEntry) throws DalException;

    /**
     * Updates the end time of a task entry.
     *
     * @param taskEntry
     * @return The updated task entry.
     * @throws DalException
     */
    TaskEntry updateTaskEntryEndTime(TaskEntry taskEntry) throws DalException;

}
