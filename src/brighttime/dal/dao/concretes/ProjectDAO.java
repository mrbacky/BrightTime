package brighttime.dal.dao.concretes;

import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.IProjectDAO;
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
public class ProjectDAO implements IProjectDAO {

    private final IConnectionManager connection;

    public ProjectDAO() throws IOException {
        this.connection = new ConnectionManager();
    }

    @Override
    public Project createProject(Project project) throws DalException {
        String sql = "INSERT INTO Project (name, clientId) "
                + "VALUES (?, ?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, project.getName());
            pstmt.setInt(2, project.getClient().getId());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                project.setId(rs.getInt(1));
            }
            return project;
        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Project> getProjects(Client client) throws DalException {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT id, name "
                + "  FROM Project "
                + "  WHERE clientId = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, client.getId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                projects.add(new Project(rs.getInt("id"), rs.getString("name"), client));
            }

        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
        return projects;
    }

}
