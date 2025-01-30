package main;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Round {
    //index 0 -> what happend -> which card plays against wich card
    //index 1 -> winner of the round

    @JsonAlias({"log"})
    private List<String> message = new LinkedList<>();

    public void addMessage(String message) {
        this.message.add(message);
    }

    @Override
    public String toString() {
        String s = "";
        for (var m : message) {
            s += m + "\n";
        }
        s += "------------------------------------------";
        return s;
    }
}
