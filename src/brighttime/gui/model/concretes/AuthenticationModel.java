package brighttime.gui.model.concretes;

import brighttime.be.EventLog;
import brighttime.be.User;
import brighttime.bll.BllException;
import brighttime.bll.BllFacade;
import brighttime.gui.model.ModelException;
import brighttime.gui.model.interfaces.IAuthenticationModel;
import brighttime.gui.model.util.InputValidator;

/**
 *
 * @author rado
 */
public class AuthenticationModel implements IAuthenticationModel {

    private final BllFacade bllManager;
    private final InputValidator inputValidator;

    public AuthenticationModel(BllFacade bllManager) {
        this.bllManager = bllManager;
        this.inputValidator = new InputValidator();
    }

    @Override
    public User authenticateUser(String username, String password) throws ModelException {
        if (!inputValidator.usernameCheck(username)) {
            throw new ModelException("The username does not exist. Please try again.");
        }
        if (!inputValidator.passwordCheck(password)) {
            throw new ModelException("The password is invalid. Please try again.");
        }
        try {
            bllManager.logEvent(new EventLog(
                    EventLog.EventType.INFORMATION,
                    "Authenticate user.",
                    username));
            return bllManager.authenticateUser(username, password);
        } catch (BllException ex) {
            throw new ModelException(ex.getMessage());
        }
    }

}
