package main.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {


    public UserProfile(String id, String name, String bio, String image, String userId) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.image = image;
        this.userId = userId;

   }

    public UserProfile(){}

    @JsonAlias({"id"})
    private String id;
    @JsonAlias({"Name"})
    private String name;
    @JsonAlias({"Bio"})
    private String bio;
    @JsonAlias({"Image"})
    private String image;
    @JsonAlias({"userId"})
    private String userId;



}
