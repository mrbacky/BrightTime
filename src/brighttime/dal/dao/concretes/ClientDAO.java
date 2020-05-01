package brighttime.dal.dao.concretes;

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

/**
 *
 * @author annem
 */
public class ClientDAO implements IClientDAO {

    private final IConnectionManager connection;

    public ClientDAO() throws IOException {
        this.connection = new ConnectionManager();
    }

    @Override
    public Client createClient(Client client) throws DalException {
        String sql = "INSERT INTO Client (name) "
                + "VALUES (?)";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, client.getName());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                client.setId(rs.getInt(1));
            }
            return client;
        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
    }

    @Override
    public List<Client> getClients() throws DalException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT id, name "
                + "FROM Client";

        try (Connection con = connection.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                clients.add(new Client(rs.getInt("id"), rs.getString("name")));
            }

        } catch (SQLException ex) {
            throw new DalException(ex.getMessage());
        }
        return clients;
    }

}
