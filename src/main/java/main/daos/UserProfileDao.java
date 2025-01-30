package main.daos;

import lombok.Getter;
import main.model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserProfileDao implements DAO<UserProfile> {

    @Getter
    private Connection connection;

    public UserProfileDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean create(UserProfile userProfile) throws SQLException {

        String query = "INSERT INTO userprofile (id,name,bio, image, userid) VALUES (?,?,?,?,?)";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1,userProfile.getId());
        stmt.setString(2,userProfile.getName());
        stmt.setString(3,userProfile.getBio());
        stmt.setString(4,userProfile.getImage());
        stmt.setString(5,userProfile.getUserId());
        return stmt.execute();

    }

    @Override
    public List<UserProfile> getAll() throws SQLException {
        return null;
    }

    @Override
    public UserProfile read(String t) throws SQLException {
        return null;
    }

    @Override
    public void update(UserProfile userProfile) {
    }

    @Override
    public void delete(String id) throws SQLException {
    }


    public int updateUserProfileByUserId(String userId, String name, String bio, String image) throws SQLException {
        String query = "UPDATE userprofile SET name = ?, bio = ?, image = ? where userid = ?";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, name);
        stmt.setString(2, bio);
        stmt.setString(3, image);
        stmt.setString(4, userId);
        return stmt.executeUpdate();
    }

}
