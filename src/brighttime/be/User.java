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
    private final StringProperty userName = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();

    private UserType type;

    public User(String firstName, String lastName, String userName, String password, UserType type) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.userName.set(userName);
        this.password.set(password);
        this.type = type;
    }

    public User(int id, String firstName, String lastName, UserType type) {
        this.id.set(id);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.type = type;
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

    public enum UserType {
        USER, ADMIN
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

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String value) {
        userName.set(value);
    }

    public StringProperty userNameProperty() {
        return userName;
    }

}
