package brighttime.be;

/**
 *
 * @author annem
 */
public class EventLog {

    private EventType type;
    private String description;
    private String username;

    public EventLog(EventType type, String description, String username) {
        this.type = type;
        this.description = description;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
