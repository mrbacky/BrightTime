package brighttime.dal.dao.concretes;

import brighttime.dal.dao.EventLogDAO;
import brighttime.be.Client;
import brighttime.be.Project;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.IEventLogDAO;
import brighttime.dal.dao.interfaces.IProjectDAO;
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
public class ProjectDAO implements IProjectDAO {

    private final IConnectionManager connection;
    private final IEventLogDAO logDAO;

    public ProjectDAO() throws IOException {
        this.connection = new ConnectionManager();
        this.logDAO = new EventLogDAO();
    }

    @Override
    public Project createProject(Project project) throws DalException {
        String sql = "INSERT INTO Project (name, clientId, hourlyRate) "
                + "VALUES (?, ?, ?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, project.getName());
            pstmt.setInt(2, project.getClient().getId());
            pstmt.setInt(3, project.getHourlyRate());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                project.setId(rs.getInt(1));
            }

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION,
                    "Created the project for the client \"" + project.getClient().getName() + "\": "
                    + project.getName() + ", " + project.getHourlyRate() + " DKK/hour.");

            return project;
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR,
                    "Unsuccessful project creation for \"" + project.getClient().getName() + "\": "
                    + project.getName() + ". " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Project> getProjectsForAClient(Client client) throws DalException {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT id, name "
                + "  FROM Project "
                + "  WHERE clientId = ?"
                + "  ORDER BY name";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, client.getId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                projects.add(new Project(rs.getInt("id"), rs.getString("name"), client));
            }

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION,
                    "Loaded all projects for the client \"" + client.getName() + "\".");

            return projects;
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR,
                    "Unsuccessful getting projects for the client \"" + client.getName() + "\". " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Project> getAllProjects() throws DalException {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT P.id AS projectId, P.name AS projectName, P.hourlyRate AS projectRate, "
                + "	C.id AS clientId, C.name AS clientName, C.hourlyRate AS clientRate "
                + "FROM Project AS P "
                + "JOIN Client AS C "
                + "	ON P.clientId = C.id "
                + "ORDER BY P.name";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int rate = rs.getInt("projectRate");
                if (rate == 0) {
                    rate = rs.getInt("clientRate");
                }
                projects.add(new Project(
                        rs.getInt("projectId"),
                        rs.getString("projectName"),
                        new Client(rs.getInt("clientId"), rs.getString("clientName"), rs.getInt("clientRate")),
                        rate));
            }

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION,
                    "Loaded all projects.");

            return projects;
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR,
                    "Unsuccessful getting all projects. " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

}
