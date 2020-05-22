package brighttime.dal.dao.concretes;

import brighttime.be.Client;
import brighttime.be.EventLog;
import brighttime.be.Project;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.IEventLogDAO;
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
            return project;
        } catch (SQLException ex) {
            //TODO: EventLog. Is this correct?
            //Unsuccessful project creation for "Lego": GGG.
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful project creation for \"" + project.getClient().getName() + "\": "
                    + project.getName() + ". " + ex.getMessage(),
                    "System"));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Project> getProjects(Client client) throws DalException {
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
            return projects;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful getting projects for the client \"" + client.getName() + "\". " + ex.getMessage(),
                    "System"));
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
            return projects;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful getting all projects. " + ex.getMessage(),
                    "System"));
            throw new DalException(ex.getMessage());
        }
    }

}
