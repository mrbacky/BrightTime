package brighttime.dal.dao.concretes;

import brighttime.be.Project;
import brighttime.be.User;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
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

    public UserDAO() throws IOException {
        this.connection = new ConnectionManager();
    }

    @Override
    public List<User> getUsers() throws DalException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT U.id, U.firstName, U.lastName, UT.userTypeName "
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
                users.add(new User(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), type));
            }

        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
        return users;
    }

    @Override
    public User authenticateUser(String username, String password) throws DalException {
        String sql = "SELECT * FROM [User] WHERE username = ? AND password = ?";

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

            if (type == 1) {
                return new User(id, firstName, lastName, User.UserType.ADMIN);
            } else {
                return new User(id, firstName, lastName, User.UserType.USER);
            }

        } catch (SQLException ex) {
            //log in database
            //Warning, Information, Error
            throw new DalException(ex.getMessage());
        }
    }

}
