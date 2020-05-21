package brighttime.dal.dao.interfaces;

import brighttime.be.EventLog;
import brighttime.dal.DalException;

/**
 *
 * @author annem
 */
public interface IEventLogDAO {

    void logEvent(EventLog log) throws DalException;

}
