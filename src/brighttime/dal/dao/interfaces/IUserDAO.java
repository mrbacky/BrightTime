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

    public User authenticateUser(String username, String password) throws DalException;

    public User createUser(User user) throws DalException;

    boolean checkUsernameAvailability(String username) throws DalException;

}
