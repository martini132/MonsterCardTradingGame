package main.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public class Controller {
    private ObjectMapper objectMapper;
    public Controller() {
        setObjectMapper(new ObjectMapper());
    }
}
