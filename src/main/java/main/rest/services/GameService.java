package main.rest.services;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.Battle;
import main.PublishSubscribe.Observer;
import main.Tuple.Tuple;
import main.daos.*;
import main.dtos.UserDeckDTO;
import main.model.FriendsList;
import main.model.Statistik;
import main.model.User;
import main.model.card.Card;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Getter
@Setter(AccessLevel.PRIVATE)
public class GameService {

    private UserDao userDao;
    private CardDao cardDao;
    private DeckDao deckDao;
    private GameDao gameDao;
    private FriendListDao friendListDao;

    private static Queue<String> battleWaiter = new LinkedList<>();


    public GameService(UserDao userDao, CardDao cardDao, DeckDao deckDao, GameDao gameDao, FriendListDao friendListDao) {
        this.userDao = userDao;
        this.cardDao = cardDao;
        this.deckDao = deckDao;
        this.gameDao = gameDao;
        this.friendListDao = friendListDao;
    }

    public boolean checkIfFriends(String friendname, String myId) {
        try {
            User user = userDao.getById(myId);
            List<FriendsList> friendsLists = friendListDao.readByUsername(user.getUsername());

            for (var friendlist : friendsLists) {
                System.out.println(friendlist.getUsername2());
                System.out.println(friendlist.getUsername1());
                System.out.println(friendname);
                if (friendlist.getUsername1().equals(friendname) || friendlist.getUsername2().equals(friendname)) {
                    return true;
                }
            }

            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean checkIfDeckIsConfigured(String userId) {
        String deckId = null;
        try {
            deckId = deckDao.getDeckIdByUserId(userId);
            List<Card> deckCards = cardDao.getByDeckid(deckId);
            if (deckCards.size() < 4) {
                return false;
            }
        } catch (SQLException throwables) {
            return false;
        }

        return true;
    }

    public Tuple<String, String> battle(String uid) {

        System.out.println("fight");

        try {
            User user = userDao.getById(uid);
            String deckId = deckDao.getDeckIdByUserId(uid);
            List<Card> deckCards = cardDao.getByDeckid(deckId);


            UserDeckDTO userDTO = new UserDeckDTO(user, deckCards);
            Observer observer = new Observer(userDTO);
            Battle.registerForBattle(observer);

            Tuple<UserDeckDTO, UserDeckDTO> players = null;

            try {
                players = observer.getBlockingQueue().take();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            UserDeckDTO winner = players.getWinner();
            UserDeckDTO looser = players.getLooser();


            if (winner.getUser().getId().equals(uid)) {
                resetDeckId(winner.getDeck());

            } else {
                resetDeckId(looser.getDeck());
            }


            if (players.getStatus().equals("draw")) {
                Statistik winnerStats = getStats(winner.getUser().getId()); // es gibt keinen gewinner hier die Variablen heißen nur so
                Statistik looserStats = getStats(looser.getUser().getId());
                winnerStats.updateDrawByOne();
                looserStats.updateDrawByOne();
                gameDao.update(winnerStats);
                gameDao.update(looserStats);
                return new Tuple<>(null, null, "unentschieden", players.getLog());

            }

            if (winner.getUser().getId().equals(uid)) {

                Statistik statistik = getStats(winner.getUser().getId());
                statistik.updateWins(statistik.getWins() + 1);
                gameDao.update(statistik);

                for (var c : winner.getDeck()) {
                    cardDao.updateUserId(c.getId(), winner.getUser().getId());
                }
                return new Tuple<>(winner.getUser().getUsername(), looser.getUser().getUsername(), "Du hast gewonnen", players.getLog());
            } else {
                Statistik statistik = getStats(looser.getUser().getId());
                statistik.updateLosses(statistik.getLosses() + 1);
                gameDao.update(statistik);

                for (var c : looser.getDeck()) {
                    cardDao.updateUserId(c.getId(), looser.getUser().getId());
                }
                return new Tuple<>(winner.getUser().getUsername(), looser.getUser().getUsername(), "Du hast verloren", players.getLog());

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Tuple<String, String> battleAgainstAFriend(String uid, String friendname) {

        try {
            User user = userDao.getById(uid);

            String deckId = deckDao.getDeckIdByUserId(uid);
            List<Card> deckCards = cardDao.getByDeckid(deckId);

            UserDeckDTO userDTO = new UserDeckDTO(user, deckCards);
            Observer observer = new Observer(userDTO);
            Battle.registerForBattleAgainstFriend(observer, friendname);
            Tuple<UserDeckDTO, UserDeckDTO> players = null;

            try {
                players = observer.getBlockingQueue().take();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            UserDeckDTO winner = players.getWinner();
            UserDeckDTO looser = players.getLooser();


            if (players.getStatus().equals("draw")) {
                return new Tuple<>(null, null, "unentschieden", players.getLog());
            } else if (winner.getUser().getId().equals(uid)) {
                return new Tuple<>(winner.getUser().getUsername(), looser.getUser().getUsername(), "Du hast gewonnen", players.getLog());
            } else {
                return new Tuple<>(winner.getUser().getUsername(), looser.getUser().getUsername(), "Du hast verloren", players.getLog());
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }


    public void updateStatistik(Statistik statistik) {
        try {
            gameDao.update(statistik);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Statistik getStats(String userId) {
        try {
            return gameDao.getByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Statistik> getScores() {
        try {
            return gameDao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean resetDeckId(List<Card> cards) {
        for (var c : cards) {
            try {
                cardDao.updateDeckIdByCardId(c.getId(), null);
            } catch (SQLException throwables) {
                return false;
            }
        }

        return true;
    }

}
