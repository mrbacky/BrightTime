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

                if (rsType.equals("Admin")) {
                    type = User.UserType.ADMIN;
                } else {
                    type = User.UserType.USER;
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
                userType = User.UserType.ADMIN;
            } else {
                userType = User.UserType.USER;
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
            ps.setInt(5, 2);

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

}
