package brighttime.dal.dao.concretes;

import brighttime.be.Client;
import brighttime.be.EventLog;
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
import brighttime.dal.dao.interfaces.IEventLogDAO;

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
            return client;
        } catch (SQLException ex) {
            //TODO: EventLog. Is this correct?
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful client creation: " + client.getName() + ". " + ex.getMessage(),
                    "System"));
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
            return clients;
        } catch (SQLException ex) {
            logDAO.logEvent(new EventLog(
                    EventLog.EventType.ERROR,
                    "Unsuccessful getting all clients. " + ex.getMessage(),
                    "System"));
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public Client updateClient(Client selectedClient) throws DalException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
