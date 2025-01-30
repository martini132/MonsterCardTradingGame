package main.daos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.model.FriendRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FriendRequestDao implements DAO<FriendRequest> {


    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Connection connection;

    public FriendRequestDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(FriendRequest friendRequest) throws SQLException {
        String query = "INSERT INTO friendrequest (id,userid, sender) VALUES (?,?,?)";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, friendRequest.getId());
        stmt.setString(2, friendRequest.getUserid());
        stmt.setString(3, friendRequest.getSender());
        return stmt.execute();


    }

    @Override
    public List<FriendRequest> getAll() throws SQLException {
        return null;
    }

    @Override
    public FriendRequest read(String id) throws SQLException {

        String query = "select * from friendrequest where id = ?";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            System.out.println("in while");
            return new FriendRequest(rs.getString("id"), rs.getString("userid"), rs.getString("sender"));
        }
        return null;

    }

    public List<FriendRequest> getByUserId(String userId) throws SQLException {
        String query = "select * from friendrequest where userid = ? ";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, userId);
        ResultSet rs = stmt.executeQuery();

        List<FriendRequest> friendRequests = new LinkedList<>();

        while (rs.next()) {
            friendRequests.add(new FriendRequest(rs.getString("id"), rs.getString("userid"), rs.getString("sender")));
        }

        return friendRequests;
    }

    public FriendRequest getByUserIdAndSender(String userId, String sender) throws SQLException {
        String query = "select * from friendrequest where userid = ? and sender = ?";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, userId);
        stmt.setString(2, sender);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            return new FriendRequest(rs.getString("id"), rs.getString("userid"), rs.getString("sender"));
        }
        return null;
    }

    @Override
    public void update(FriendRequest friendRequest) {

    }

    @Override
    public void delete(String id) throws SQLException {
        String query = "DELETE FROM friendrequest WHERE id = ?";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, id);
        stmt.executeUpdate();
    }
}
