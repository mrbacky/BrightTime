package brighttime.dal.dao;

import brighttime.dal.DalException;

/**
 *
 * @author annem
 */
public interface IEventLogDAO {

    /**
     * Logs an event in the database.
     *
     * @param type The type of the event.
     * @param description The description of the event.
     * @throws DalException
     */
    void logEvent(EventLogDAO.EventType type, String description) throws DalException;

}
