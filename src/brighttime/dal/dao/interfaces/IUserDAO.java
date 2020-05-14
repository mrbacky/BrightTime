package brighttime.dal.dao.interfaces;

import brighttime.be.User;
import brighttime.dal.DalException;
import java.util.List;

/**
 *
 * @author annem
 */
public interface IUserDAO {

    List<User> getUsers() throws DalException;

}
