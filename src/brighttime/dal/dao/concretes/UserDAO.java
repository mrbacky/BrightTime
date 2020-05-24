package brighttime.dal.dao.concretes;

import brighttime.be.EventLog;
import brighttime.be.User;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.IEventLogDAO;
import brighttime.dal.dao.interfaces.IUserDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author annem
 */
public class UserDAO implements IUserDAO {

    private final IConnectionManager connection;
    private final IEventLogDAO logDAO;

    public UserDAO() throws IOException {
        this.connection = new ConnectionManager();
        this.logDAO = new EventLogDAO();
    }

    @Override
    public List<User> getUsers() throws DalException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT U.id, U.firstName, U.lastName, U.username, UT.userTypeName "
                + "FROM [User] AS U "
                + "JOIN UserType AS UT "
                + "ON U.userTypeId = UT.id "
                + "ORDER BY U.firstName";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String rsType = rs.getString("userTypeName");
                User.UserType type;

                if (rsType.equals("ADMINISTRATOR")) {
                    type = User.UserType.ADMINISTRATOR;
                } else {
                    type = User.UserType.STANDARD;
                }
                users.add(new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("username"), type));
            }
            return users;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful getting users. " + ex.getMessage(),
                    "System"));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public User authenticateUser(String username, String password) throws DalException {
        String sql = "SELECT id, firstName, lastName, userTypeId "
                + "FROM [User] WHERE username = ? AND password = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int id = rs.getInt("id");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            int type = rs.getInt("userTypeId");

            User.UserType userType;
            if (type == 1) {
                userType = User.UserType.ADMINISTRATOR;
            } else {
                userType = User.UserType.STANDARD;
            }
            return new User(id, firstName, lastName, username, userType);
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful authentication: " + username + ". " + ex.getMessage(),
                    "System"));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public User createUser(User user) throws DalException {

        String sql = "INSERT INTO [User] (firstName, lastName, username, password, userTypeId) "
                + "VALUES (?,?,?,?,?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getUsername());
            ps.setString(4, user.getPassword());
            //  UserType USER = id 2
            if (user.getType() == User.UserType.ADMINISTRATOR) {
                ps.setInt(5, 1);
                System.out.println("int for admin");
            } else {
                ps.setInt(5, 2);
                System.out.println("int for user");

            }
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs != null && rs.next()) {
                user.setId(rs.getInt(1));
            }
            return user;
        } catch (Exception ex) {
            logDAO.logEvent(new EventLog(EventLog.EventType.ERROR,
                    "Unsuccessful user creation: " + user.getUsername() + ". " + ex.getMessage(),
                    "System"));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public int checkUsernameAvailability(String username) throws DalException {
        String sql = "SELECT id "
                + "FROM [User] "
                + "WHERE username = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful username availability check. " + ex.getMessage(),
                    "System"));
            throw new DalException(ex.getMessage());
        }
        return 0;
    }

    @Override
    public User updateUserDetails(User updatedUser) throws DalException {

        String sql = "UPDATE [User]\n"
                + "SET firstName = ?, lastName = ?, username = ?, userTypeId = ?\n"
                + "WHERE id = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, updatedUser.getFirstName());
            ps.setString(2, updatedUser.getLastName());
            ps.setString(3, updatedUser.getUsername());

            if (updatedUser.getType() == User.UserType.ADMINISTRATOR) {
                ps.setInt(4, 1);
            } else {
                ps.setInt(4, 2);
            }
            ps.setInt(5, updatedUser.getId());

            ps.executeUpdate();

        } catch (Exception ex) {
            throw new DalException(ex.getMessage());
        }
        return updatedUser;
    }

    @Override
    public User deleteUser(User selectedUser) throws DalException {
        //TODO: Ask if a user with tasks should be deleted or not? Maybe delete the user and keep the tasks.
        //Or not allow user deletion if the user has tasks.
        String sql = "DELETE FROM [User] WHERE id = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, selectedUser.getId());
            ps.execute();
        } catch (Exception ex) {
            throw new DalException(ex.getMessage());
        }
        return selectedUser;
    }

}
