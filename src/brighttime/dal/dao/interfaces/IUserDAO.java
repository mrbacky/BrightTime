package brighttime.dal.dao.interfaces;

import brighttime.be.User;
import brighttime.dal.DalException;
import java.util.List;

/**
 *
 * @author annem
 */
public interface IUserDAO {

    User createUser(User user) throws DalException;

    List<User> getUsers() throws DalException;

    User authenticateUser(String username, String password) throws DalException;

    User updateUserDetails(User user) throws DalException;

    User deleteUser(User user) throws DalException;

}
