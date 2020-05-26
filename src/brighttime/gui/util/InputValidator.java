package brighttime.gui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author annem
 */
public class InputValidator {

    public boolean usernameCheck(String username) {
        //TODO: Has to contain @grumsendev.com or just an email?
        //"/w" matches any single letter (upper and lower case), number or underscore.
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public boolean passwordCheck(String password) {
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }
        //Password has to contain at least one letter and one number.
        return password.matches(".*([a-zA-Z].*[0-9]|[0-9].*[a-zA-Z]).*");
    }

}
