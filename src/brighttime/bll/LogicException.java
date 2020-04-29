package brighttime.bll;

/**
 *
 * @author annem
 */
public class LogicException extends Exception {

    public LogicException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (LogicException)";
    }

}
