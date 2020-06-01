package brighttime.dal.dao;

import brighttime.dal.DalException;

/**
 *
 * @author annem
 */
public interface IEventLogDAO {

    void logEvent(EventLogDAO.EventType type, String description) throws DalException;

}
