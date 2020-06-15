package brighttime.dal.dao.concretes;

import brighttime.dal.dao.EventLogDAO;
import brighttime.be.Client;
import brighttime.dal.ConnectionManager;
import brighttime.dal.DalException;
import brighttime.dal.IConnectionManager;
import brighttime.dal.dao.interfaces.IClientDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import brighttime.dal.dao.IEventLogDAO;
import java.util.Arrays;

/**
 *
 * @author annem
 */
public class ClientDAO implements IClientDAO {

    private final IConnectionManager connection;
    private final IEventLogDAO logDAO;

    public ClientDAO() throws IOException {
        this.connection = new ConnectionManager();
        this.logDAO = new EventLogDAO();
    }

    @Override
    public Client createClient(Client client) throws DalException {
        String sql = "INSERT INTO Client (name, hourlyRate) "
                + "VALUES (?,?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, client.getName());
            pstmt.setInt(2, client.getHourlyRate());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                client.setId(rs.getInt(1));
            }

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION,
                    "Created the client: " + client.getName() + ", " + client.getHourlyRate() + " DKK/hour.");

            return client;
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR,
                    "Unsuccessful client creation: " + client.getName() + ". " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Client> getClients() throws DalException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT id, name, hourlyRate "
                + "  FROM Client"
                + "  ORDER BY name";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                clients.add(new Client(rs.getInt("id"), rs.getString("name"), rs.getInt("hourlyRate")));
            }

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION,
                    "Loaded all clients.");

            return clients;
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR,
                    "Unsuccessful getting all clients. " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public void updateClient(Client client) throws DalException {
        String sql = "UPDATE Client SET name = ?, hourlyRate = ? WHERE id = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, client.getName());
            pstmt.setInt(2, client.getHourlyRate());
            pstmt.setInt(3, client.getId());
            pstmt.executeUpdate();

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION,
                    "Updated the client \"" + client.getName() + "\".");
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR,
                    "Unsuccessful updating the client \"" + client.getName() + "\". " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public Client deleteClient(Client client) throws DalException {
        //TODO: Change deletion method.
        String sql = "DELETE FROM Client WHERE id = ?";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, client.getId());
            pstmt.executeUpdate();

            logDAO.logEvent(EventLogDAO.EventType.INFORMATION,
                    "Deleted the client \"" + client.getName() + "\".");

            return client;
        } catch (SQLException ex) {
            logDAO.logEvent(EventLogDAO.EventType.ERROR,
                    "Unsuccessful deleting the client \"" + client.getName() + "\". " + Arrays.toString(ex.getStackTrace()));
            throw new DalException(ex.getMessage());
        }
    }

}
