package brighttime.be;

/**
 *
 * @author annem
 */
public class EventLog {

    private EventType type;
    private String description;

    public EventLog(EventType type, String description) {
        this.type = type;
        this.description = description;
    }

    public enum EventType {
        INFORMATION, WARNING, ERROR
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
