package brighttime.bll;

/**
 *
 * @author annem
 */
public class BllException extends Exception {

    public BllException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (LogicException)";
    }

}
