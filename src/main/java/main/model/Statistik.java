package main.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class Statistik {

    @JsonAlias({"name"})
    private String name;
    @JsonAlias({"elo"})
    private int elo;
    @JsonAlias({"wins"})
    private int wins;
    @JsonAlias({"looses"})
    private int losses;
    @JsonAlias({"userid"})
    private String userid;
    @JsonAlias({"id"})
    private String id;
    @JsonAlias({"winloseratio"})
    private double winloseratio;
    @JsonAlias({"draw"})
    private int draw;


    public Statistik(String name, int elo, int wins, int losses, String userid, String id, double winloseratio, int draw) {
        this.name = name;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
        this.userid = userid;
        this.id = id;
        this.winloseratio = winloseratio;
        this.draw = draw;
    }

    public void updateWins(int newWin) {
        setWins(newWin);

        if (getLosses() == 0)
            setWinloseratio(0);
        else
            setWinloseratio(getWins() * 1.0 / getLosses());
        setElo(getElo() + 3);
    }

    public void updateLosses(int newLoose) {
        setLosses(newLoose);
        if (getLosses() == 0)
            setWinloseratio(0);
        else
            setWinloseratio(getWins() * 1.0 / getLosses());
        setElo(getElo() - 5);
    }

    public void updateDrawByOne() {
        setDraw(getDraw() + 1);
    }

}
