package main.daos;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.model.Package;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class PackageDao implements DAO<Package> {

    private Connection connection;

    public PackageDao(Connection connection) {
        setConnection(connection);
    }

    @Override
    public boolean create(Package aPackage) throws SQLException {
        String query = "INSERT INTO Package(packageid, cost) VALUES (?,?)";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, aPackage.getId());
        stmt.setInt(2, aPackage.getPACKAGE_COST());
        return stmt.execute();
    }

    @Override
    public List<Package> getAll() throws SQLException {
        List<Package> packages = new LinkedList<>();
        String query = "select packageid from package";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            packages.add(new Package(rs.getString(1)));
        }

        return packages;
    }

    @Override
    public Package read(String t) throws SQLException {
        return null;
    }

    @Override
    public void update(Package apackage) {

    }

    @Override
    public void delete(String id) throws SQLException {
        String query = "DELETE FROM package WHERE packageid = ?";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, id);
        stmt.execute();

    }


    public Package getOnePackage() throws SQLException {
        String query = "select * from package limit 1";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        ResultSet rs = stmt.executeQuery();


        while (rs.next()) {
            return new Package(rs.getString(1));
        }
        return null;
    }
}
