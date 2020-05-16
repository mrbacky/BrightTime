package brighttime.gui.model.concretes;

import brighttime.be.User;
import brighttime.bll.BllException;
import brighttime.bll.BllFacade;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IAuthenticationModel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rado
 */
public class AuthenticationModel implements IAuthenticationModel {

    private final BllFacade bllManager;

    public AuthenticationModel(BllFacade bllManager) {
        this.bllManager = bllManager;
    }

    @Override
    public User authenticateUser(String username, String password) throws ModelException {
        try {
            return bllManager.authenticateUser(username, password);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

}
