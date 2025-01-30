package main.daos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.model.FriendsList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FriendListDao implements DAO<FriendsList> {

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Connection connection;

    public FriendListDao(Connection connection) {
        setConnection(connection);
    }

    @Override
    public boolean create(FriendsList friendsList) throws SQLException {

        String query = "INSERT INTO friends (id,status, username1,username2) VALUES (?,?,?,?)";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, friendsList.getId());
        stmt.setString(2, friendsList.getStatus());
        stmt.setString(3, friendsList.getUsername1());
        stmt.setString(4, friendsList.getUsername2());
        return stmt.execute();

    }

    @Override
    public List<FriendsList> getAll() throws SQLException {
        return null;
    }

    @Override
    public FriendsList read(String id) throws SQLException {
        return null;
    }

    public List<FriendsList> readByUsername(String usernmae) throws SQLException {
        String query = "select * from friends where username1 = ? or username2 = ? ";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, usernmae);
        stmt.setString(2, usernmae);
        ResultSet rs = stmt.executeQuery();

        List<FriendsList> friendsLists = new LinkedList<>();

        while (rs.next()) {
            friendsLists.add(new FriendsList(rs.getString("id"), rs.getString("username1"),
                    rs.getString("username2"), rs.getString("status")));
        }

        return friendsLists;
    }


    @Override
    public void update(FriendsList friendsList) {
    }

    public int updateStatus(String userId, String friendname, String status) throws SQLException {
        String query = "UPDATE friends SET status = ? WHERE userid = ? and friendname = ?";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, status);
        stmt.setString(2, userId);
        stmt.setString(3, friendname);
        return stmt.executeUpdate();
    }

    @Override
    public void delete(String id) throws SQLException {
    }


}
