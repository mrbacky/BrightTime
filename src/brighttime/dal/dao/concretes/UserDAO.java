package brighttime.dal.dao.concretes;

import brighttime.dal.dao.EventLogDAO;
import brighttime.be.User;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.IEventLogDAO;
import brighttime.dal.dao.interfaces.IUserDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author annem
 */
public class UserDAO implements IUserDAO {

    private final IConnectionManager connection;
    private final IEventLogDAO logDAO;
    private final int administrator = 1;
    private final int standard = 2;
    private final int activeStatus = 1;
    private final int inactiveStatus = 2;

    public UserDAO() throws IOException {
        this.connection = new ConnectionManager();
        this.logDAO = new EventLogDAO();
    }

    @Override
    public User createUser(User user) throws DalException {
        String sql = "INSERT INTO [User] (firstName, lastName, username, password, userTypeId, statusId) "
                + "VALUES (?,?,?,?,?,?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getUsername());
            pstmt.setString(4, user.getPassword());
            if (user.getType() == User.UserType.ADMINISTRATOR) {
                pstmt.setInt(5, administrator);
            } else {
                pstmt.setInt(5, standard);
            }
            pstmt.setInt(6, activeStatus);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                user.setId(rs.getInt(1));
            }

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION, "Created the user: " + user.getUsername() + ".");

            return user;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 2627) {
                throw new DalException("Someone already has this username. Please try another username.");
            }
            logDAO.logEvent(EventLogDAO.EventType.ERROR, "Unsuccessful user creation: " + user.getUsername() + ". " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<User> getUsers() throws DalException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT U.id, U.firstName, U.lastName, U.username, UT.userTypeName "
                + "FROM [User] AS U "
                + "JOIN UserType AS UT "
                + "ON U.userTypeId = UT.id "
                + "WHERE statusId = ? "
                + "ORDER BY U.firstName";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, activeStatus);

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

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION, "Loaded all users.");

            return users;
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR, "Unsuccessful getting users. " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public User authenticateUser(String username, String password) throws DalException {
        String sql = "SELECT id, firstName, lastName, userTypeId "
                + "FROM [User] WHERE username = ? AND password = ? AND statusId = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, activeStatus);

            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int id = rs.getInt("id");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            int type = rs.getInt("userTypeId");

            User.UserType userType;
            if (type == administrator) {
                userType = User.UserType.ADMINISTRATOR;
            } else {
                userType = User.UserType.STANDARD;
            }

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION, "Authenticated user: " + username + ".");

            return new User(id, firstName, lastName, username, userType);
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR, "Unsuccessful authentication: " + username + ". " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public void updateUserDetails(User user) throws DalException {
        String sql = "UPDATE [User] "
                + "SET firstName = ?, lastName = ?, username = ?, userTypeId = ? "
                + "WHERE id = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getUsername());

            if (user.getType() == User.UserType.ADMINISTRATOR) {
                pstmt.setInt(4, administrator);
            } else {
                pstmt.setInt(4, standard);
            }
            pstmt.setInt(5, user.getId());

            pstmt.executeUpdate();
            logDAO.logEvent(EventLogDAO.EventType.INFORMATION, "Updated the information of the user: " + user.getUsername() + ".");
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR, "Unsuccessful information update of the user: " + user.getUsername() + ". " + Arrays.toString(ex.getStackTrace()));
            if (ex.getErrorCode() == 2627) {
                throw new DalException("Someone already has this username. Please try another username.");
            }
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public User deleteUser(User user) throws DalException {
        String sql = "DELETE FROM [User] WHERE id = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, user.getId());
            pstmt.executeUpdate();

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION, "Deleted the user: " + user.getUsername() + ".");

            return user;
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 547) {
                deactivateUser(user);
                return user;
            } else {
                logDAO.logEvent(EventLogDAO.EventType.ERROR, "Unsuccessful deleting the user \"" + user.getUsername() + "\". " + Arrays.toString(ex.getStackTrace()));
                throw new DalException(ex.getMessage());
            }
        }
    }

    private User deactivateUser(User user) throws DalException {
        String sql = "UPDATE [User] "
                + "SET statusId = ? "
                + "WHERE id = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement psmt = con.prepareStatement(sql);
            psmt.setInt(1, inactiveStatus);
            psmt.setInt(2, user.getId());
            psmt.executeUpdate();

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION, "Deactivated the user: " + user.getUsername() + ".");

            return user;
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR, "Unsuccessful deactivating the user \"" + user.getUsername() + "\". " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

}
