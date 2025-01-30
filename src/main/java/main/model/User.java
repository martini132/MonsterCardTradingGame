package main.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class User {

    @JsonAlias({"username"})
    private String username;
    @JsonAlias({"coins"})
    private int coins = 20;
    @JsonAlias({"password"})
    private String password;
    @JsonAlias({"id"})
    private String id;


    @JsonCreator
    public User(@JsonProperty("Username") String username, @JsonProperty("Password") String password) {
        setUsername(username);
        setPassword(password);
        UUID uuid = UUID.randomUUID();
        setId(uuid.toString());
    }


    public User(String username, int coins, String password, String id) {
        this.username = username;
        this.coins = coins;
        this.password = password;
        this.id = id;
    }
}
