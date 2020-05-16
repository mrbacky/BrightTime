package brighttime.gui.model.concretes;

import brighttime.be.User;
import brighttime.bll.BllFacade;
import brighttime.gui.model.interfaces.IAuthenticationModel;

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
    public User authenticateUser(String username, String password) {
        return bllManager.authenticateUser(username, password);
    }

}
