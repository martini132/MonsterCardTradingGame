package main;

import main.PublishSubscribe.Observer;
import main.dtos.UserDeckDTO;
import main.model.card.Card;
import main.rest.http.Method;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Battle {

    private static BlockingQueue<Observer> waiter = new LinkedBlockingQueue<>();
    private static ConcurrentHashMap<String, Observer> watingForFriend = new ConcurrentHashMap<>();

    public static void registerForBattle(Observer observer) {
        waiter.add(observer);

        if (waiter.size() >= 2) {

            Observer observer1 = null;
            Observer observer2 = null;
            try {
                observer1 = waiter.take();
                observer2 = waiter.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            battle(observer1.getUser(), observer2.getUser(), observer1, observer2);
        }
    }

    public static void registerForBattleAgainstFriend(Observer observer, String friendname) {

        if (watingForFriend.get(observer.getUser().getUser().getUsername()) != null) {
            Observer observer1 = observer;
            Observer observer2 = watingForFriend.get(observer.getUser().getUser().getUsername());
            watingForFriend.remove(observer.getUser().getUser().getUsername());

            battle(observer1.getUser(), observer2.getUser(), observer1, observer2);

        } else {
            watingForFriend.put(friendname, observer);
        }
    }

    private static void battle(UserDeckDTO u1, UserDeckDTO u2, Observer observer1, Observer observer2) {


        boolean _100Rounds = true;

        List<Round> roundProtocol = new LinkedList<>();

        for (int i = 0; i < 100; i++) {


            Collections.shuffle(u1.getDeck());
            Collections.shuffle(u2.getDeck());


            if (u1.getDeck().size() == 0 || u2.getDeck().size() == 0) {
                _100Rounds = false;
                break;
            }

            Card user1Card = u1.getDeck().get(0);
            Card user2Card = u2.getDeck().get(0);

            UserDeckDTO winner = null;


            if (user1Card.getClass().getSimpleName().contains("Spell") &&
                    user2Card.getClass().getSimpleName().contains("Spell")) {
                winner = elementFight(u1, u2, user1Card, user2Card);

            } else {
                winner = monsterFight(u1, u2, user1Card, user2Card);
            }

            Round round = new Round();

            String card1Log = user1Card.getName() + " Damage: " + user1Card.getDamage();
            String card2Log = user2Card.getName() + " Damage: " + user2Card.getDamage();
            round.addMessage(u1.getUser().getUsername() + " " + card1Log + " vs. " + u2.getUser().getUsername() + " " + card2Log);


            if (winner == null) {
                round.addMessage("Unentschiede keine Karte hat gewonnen");
            } else if (winner.getUser().getId().equals(u1.getUser().getId())) {
                round.addMessage("Spieler Karte 1 hat gewonnen");
            } else {
                round.addMessage("Spieler karte 2 hat gewonnen");
            }
            roundProtocol.add(round);
        }

        System.out.println(roundProtocol.size());
        System.out.println(u1.getDeck().size());
        System.out.println(u2.getDeck().size());
        System.out.println(_100Rounds);

        if (_100Rounds) {
            observer1.setResult(u1, u2, roundProtocol, "draw");
            observer2.setResult(u1, u2, roundProtocol, "draw");
        } else {
            if (u1.getDeck().size() > u2.getDeck().size()) {
                observer1.setResult(u1, u2, roundProtocol, "Du hast gewonnen");
                observer2.setResult(u1, u2, roundProtocol, "Du hast verloren");
            } else if (u2.getDeck().size() > u1.getDeck().size()) {
                observer1.setResult(u2, u1, roundProtocol, "Du hast verloren");
                observer2.setResult(u2, u1, roundProtocol, "Du hast gewonnen");
            }
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter("./logoutput/" + UUID.randomUUID());
            for (var round : roundProtocol) {
                writer.append(round.toString());
                writer.append("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static UserDeckDTO monsterFight(UserDeckDTO u1, UserDeckDTO u2, Card c1, Card c2) {

        if (c2.getWeakness() != null && c1.getNameAndType().contains(c2.getWeakness().toLowerCase(Locale.ROOT))) {
            return u1;
        } else if (c1.getWeakness() != null && c2.getNameAndType().contains(c1.getWeakness().toLowerCase(Locale.ROOT))) {

            return u2;
        } else {
            if (c1.getClass().getSimpleName().contains("Monster") && c2.getClass().getSimpleName().contains("Monster"))
                return winner(u1, u2, c1.getDamage(), c2.getDamage(), c1, c2);
            else
                return elementFight(u1, u2, c1, c2);
        }
    }

    private static UserDeckDTO elementFight(UserDeckDTO u1, UserDeckDTO u2, Card c1, Card c2) {
        Element t1 = c1.getType();
        Element t2 = c2.getType();

        if (t1 == c2.getTypeWeakness()) {
            return winner(u1, u2, c1.getDamage() * 2.0, c2.getDamage() / 2.0, c1, c2);
        } else if (t2 == c1.getTypeWeakness()) {
            return winner(u1, u2, c1.getDamage() / 2.0, c2.getDamage() * 2, c1, c2);
        } else {
            return winner(u1, u2, c1.getDamage(), c2.getDamage(), c1, c2);
        }
    }

    private static UserDeckDTO winner(UserDeckDTO u1, UserDeckDTO u2, double damageC1, double damageC2, Card c1, Card c2) {
        if (damageC1 > damageC2) {
            u1.getDeck().add(c2);
            u2.getDeck().remove(c2);
            return u1;
        } else if (damageC1 < damageC2) {
            u2.getDeck().add(c1);
            u1.getDeck().remove(c1);
            return u2;
        }
        return null;
    }


}
