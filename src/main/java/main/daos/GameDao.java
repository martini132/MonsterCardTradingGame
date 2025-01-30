package main.daos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.model.Statistik;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class GameDao implements DAO<Statistik> {

    private Connection connection;

    public GameDao(Connection connection) {
        setConnection(connection);
    }


    @Override
    public boolean create(Statistik statistik) throws SQLException {
        String query = "INSERT INTO statistik (statistikid,userid,name,wins,losses,elo,winloseratio) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, statistik.getId());
        stmt.setString(2, statistik.getUserid());
        stmt.setString(3, statistik.getName());
        stmt.setInt(4, statistik.getWins());
        stmt.setInt(5, statistik.getLosses());
        stmt.setInt(6, statistik.getElo());
        stmt.setDouble(7, statistik.getWinloseratio());
        return stmt.execute();
    }

    @Override
    public List<Statistik> getAll() throws SQLException {
        List<Statistik> stats = new LinkedList<>();
        String query = "select * from statistik ";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            var stat = new Statistik(rs.getString("name"), rs.getInt("elo"), rs.getInt("wins"), rs.getInt("losses"), rs.getString("userid"), rs.getString("statistikid"), rs.getDouble("winloseratio"), rs.getInt("draw"));
            stats.add(stat);
        }

        return stats;
    }

    @Override
    public Statistik read(String t) throws SQLException {
        return null;
    }

    @Override
    public void update(Statistik statistik) throws SQLException {
        String query = "UPDATE statistik SET wins=?,losses = ?, winloseratio = ?, elo=?, draw = ? where statistikid = ?";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setInt(1, statistik.getWins());
        stmt.setInt(2, statistik.getLosses());
        stmt.setDouble(3, statistik.getWinloseratio());
        stmt.setInt(4, statistik.getElo());
        stmt.setInt(5, statistik.getDraw());
        stmt.setString(6, statistik.getId());
        stmt.executeUpdate();
    }

    @Override
    public void delete(String id) throws SQLException {
    }


    public Statistik getByUserId(String userId) throws SQLException {
        String query = "select * from statistik where userid = ? ";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            return new Statistik(rs.getString("name"), rs.getInt("elo"), rs.getInt("wins"), rs.getInt("losses"),
                    rs.getString("userid"), rs.getString("statistikid"), rs.getDouble("winloseratio"), rs.getInt("draw"));
        }
        return null;
    }
}
