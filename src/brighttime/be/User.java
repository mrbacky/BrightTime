package brighttime.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author annem
 */
public class User {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty userType = new SimpleStringProperty();

    private UserType type;

    public enum UserType {
        ADMINISTRATOR {
            @Override
            public String toString() {
                return "Admin";
            }
        },
        STANDARD {
            @Override
            public String toString() {
                return "Standard";
            }
        }
    }

    //  contructor for creating User
    public User(String firstName, String lastName, String username, String password, UserType type) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.username.set(username);
        this.password.set(password);
        this.type = type;

    }
    //contructor for viewing users in Administration section

    //  constructor for getting user from DB
    public User(int id, String firstName, String lastName, String username, UserType type) {
        this.id.set(id);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.username.set(username);
        this.type = type;
        //  setting String usertype from enum
        this.userType.set(type.toString());
    }

    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String value) {
        firstName.set(value);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String value) {
        lastName.set(value);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String value) {
        password.set(value);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String value) {
        username.set(value);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getUserType() {
        return userType.get();
    }

    public void setUserType(String value) {
        userType.set(value);
    }

    public StringProperty userTypeProperty() {
        return userType;
    }

}
