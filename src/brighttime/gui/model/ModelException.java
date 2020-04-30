package brighttime.gui.model;

/**
 *
 * @author annem
 */
public class ModelException extends Exception {

    public ModelException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (ModelException)";
    }

}
