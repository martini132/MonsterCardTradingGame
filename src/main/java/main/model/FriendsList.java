package main.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter(AccessLevel.PRIVATE)
public class FriendsList {
    @JsonAlias({"id"})
    private String id;
    @JsonAlias({"username1"})
    private String username1;
    @JsonAlias({"username2"})
    private String username2;
    @JsonAlias({"status"})
    private String status;

    public FriendsList() {
    }

    public FriendsList(String id, String username1, String username2, String status) {
        this.id = id;
        this.username1 = username1;
        this.username2 = username2;
        this.status = status;
    }
}
