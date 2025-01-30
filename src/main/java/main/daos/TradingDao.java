package main.daos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.model.Trading;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class TradingDao implements DAO<Trading> {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Connection connection;

    public TradingDao(Connection connection) {
        setConnection(connection);
    }

    @Override
    public boolean create(Trading trading) throws SQLException {

        String query = "INSERT INTO trading (id,cardtotrade,type,minimumdamage) VALUES (?,?,?,?)";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, trading.getId());
        stmt.setString(2, trading.getCardToTrade());
        stmt.setString(3, trading.getType());
        stmt.setInt(4, trading.getMinimumDamage());

        return stmt.execute();
    }

    @Override
    public List<Trading> getAll() throws SQLException {

        List<Trading> trades = new LinkedList<>();
        String query = "select * from trading ";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            var trade = new Trading(rs.getString("id"), rs.getString("cardtotrade"),
                    rs.getString("type"), rs.getInt("minimumdamage"));

            trades.add(trade);
        }

        return trades;
    }

    @Override
    public Trading read(String id) throws SQLException {

        String query = "select * from trading where id = ?";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

//        String id, String cardToTrade, String type, int minimumDamage

        while (rs.next()) {
            return new Trading(rs.getString("id"), rs.getString("cardtotrade"), rs.getString("type"), rs.getInt("minimumdamage"));
        }

        return null;
    }

    @Override
    public void update(Trading trading) {

    }

    @Override
    public void delete(String id) throws SQLException {
        String query = "DELETE FROM trading WHERE id=?";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, id);
        stmt.execute();
    }
}
