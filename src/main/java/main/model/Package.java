package main.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;



@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class Package {
    @JsonAlias({"cost"})
    final int PACKAGE_COST = 5;
    @JsonAlias({"packageId"})
    private String id;


    public Package(){
    }

    public Package(String id){
        setId(id);
    }
}
