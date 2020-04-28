package brighttime.dal;

/**
 *
 * @author annem
 */
public class DalException extends Exception {

    public DalException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (DALException)";
    }

}
