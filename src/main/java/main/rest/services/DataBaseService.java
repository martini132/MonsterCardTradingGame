package main.rest.services;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Setter
@Getter
public class DataBaseService {
    private Connection connection;

    public DataBaseService() throws SQLException {
        setConnection(
                DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/mtcg_db",
                        "martini",
                        "martini"
                )
        );
    }

}
