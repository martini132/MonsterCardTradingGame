package main.PublishSubscribe;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.Round;
import main.Tuple.Tuple;
import main.dtos.UserDeckDTO;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class Observer implements Listener {

    private UserDeckDTO user;
    private boolean isFinish;
    private BlockingQueue<Tuple<UserDeckDTO, UserDeckDTO>> blockingQueue = new LinkedBlockingQueue(1);


    public Observer(UserDeckDTO user) {
        this.user = user;
    }

    @Override
    public void setResult(UserDeckDTO winner, UserDeckDTO looser, List<Round> log, String status) {

        try {
            blockingQueue.put(new Tuple<>(winner, looser,status , log));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
