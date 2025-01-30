package main.daos;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {


    boolean create(T t) throws SQLException;

    List<T> getAll() throws SQLException;

    T read(String t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(String id) throws SQLException;
}
