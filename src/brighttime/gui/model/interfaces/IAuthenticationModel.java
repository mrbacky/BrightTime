package brighttime.gui.model.interfaces;

import brighttime.be.User;
import brighttime.gui.model.ModelException;

/**
 *
 * @author rados
 */
public interface IAuthenticationModel {

    /**
     * Authenticates the user.
     *
     * @param username The username.
     * @param password The password.
     * @return The authenticated user.
     * @throws ModelException
     */
    User authenticateUser(String username, String password) throws ModelException;

}
