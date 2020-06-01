package brighttime.dal.dao.interfaces;

import brighttime.dal.DalException;
import brighttime.dal.dao.concretes.EventLogDAO;

/**
 *
 * @author annem
 */
public interface IEventLogDAO {

    void logEvent(EventLogDAO.EventType type, String description) throws DalException;

}
