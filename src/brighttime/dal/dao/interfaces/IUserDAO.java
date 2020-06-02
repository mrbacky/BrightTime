package brighttime.dal.dao.interfaces;

import brighttime.be.User;
import brighttime.dal.DalException;
import java.util.List;

/**
 *
 * @author annem
 */
public interface IUserDAO {

    /**
     * Creates the new user in the database.
     *
     * @param user The user to create.
     * @return The created user.
     * @throws DalException
     */
    User createUser(User user) throws DalException;

    /**
     * Gets all the active users from the database.
     *
     * @return A list of active users.
     * @throws DalException
     */
    List<User> getUsers() throws DalException;

    /**
     * Authenticates the user.
     *
     * @param username The username.
     * @param password The password.
     * @return The authenticated user.
     * @throws DalException
     */
    User authenticateUser(String username, String password) throws DalException;

    /**
     * Updates the user.
     *
     * @param user The user to be updated.
     * @return The updated user.
     * @throws DalException
     */
    User updateUserDetails(User user) throws DalException;

    /**
     * Deletes the user. Users with tasks are permanently deleted, while users
     * with tasks are marked as inactive and removed from the application.
     *
     * @param user The user to be deleted.
     * @return The deleted user.
     * @throws DalException
     */
    User deleteUser(User user) throws DalException;

}
