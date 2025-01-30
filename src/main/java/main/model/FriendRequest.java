package main.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;

@Getter
public class FriendRequest {
    @JsonAlias({"id"})
    private String id;
    @JsonAlias({"userid"})
    private String userid;
    @JsonAlias({"sender"})
    private String sender;

    public FriendRequest(String id, String userid, String sender) {
        this.id = id;
        this.userid = userid;
        this.sender = sender;
    }

    public FriendRequest(){}
}
