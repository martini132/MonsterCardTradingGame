package main.PublishSubscribe;

import main.Round;
import main.dtos.UserDeckDTO;

import java.util.List;

public interface Listener {

    void setResult(UserDeckDTO winner, UserDeckDTO looser, List<Round> log, String status);

}
