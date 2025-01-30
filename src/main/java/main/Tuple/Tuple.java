package main.Tuple;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.Round;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter(AccessLevel.PRIVATE)
public class Tuple<T1, T2> {


    private String status;
    private T1 winner;
    private T2 looser;
    private List<Round> log = new LinkedList<>();


    public Tuple(T1 winner, T2 looser, String status, List<Round> log) {
        setWinner(winner);
        setLooser(looser);
        setStatus(status);
        setLog(log);
    }
}
