package main;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.daos.*;
import main.Controller.*;
import main.rest.http.ContentType;
import main.rest.http.HttpStatus;
import main.rest.server.Request;
import main.rest.server.Response;
import main.rest.server.ServerApp;
import main.rest.services.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PRIVATE)
public class App implements ServerApp {

    @Setter(AccessLevel.PRIVATE)
    private UserController userController;
    private CardController cardController;
    private PackageController packageController;
    private GameController gameController;
    private DeckController deckController;
    private TradingController tradingController;
    private FriendController friendController;

    private Connection connection;

    public App() {
        try {
            setConnection(new DataBaseService().getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        var userDao = new UserDao(getConnection());
        var cardDao = new CardDao(getConnection());
        var packageDao = new PackageDao(getConnection());
        var deckDao = new DeckDao(getConnection());
        var gameDao = new GameDao(getConnection());
        var tradingDao = new TradingDao(getConnection());
        var friendListDao = new FriendListDao(getConnection());
        var friendRequestDao = new FriendRequestDao(getConnection());
        var userProfileDao = new UserProfileDao(getConnection());

        var packageService = new PackageService(packageDao, cardDao, userDao);
        var deckService = new DeckService(deckDao, cardDao);
        var battleService = new GameService(userDao, cardDao, deckDao, gameDao,friendListDao);
        var cardService = new CardService(cardDao);
        var tradingService = new TradingService(tradingDao, cardDao);
        var userService = new UserService(userDao, gameDao, userProfileDao);
        var friendService = new FriendService(friendListDao, userDao, friendRequestDao);


        packageController = new PackageController(packageService);
        deckController = new DeckController(deckService);
        gameController = new GameController(battleService);
        cardController = new CardController(cardService);
        tradingController = new TradingController(tradingService);
        userController = new UserController(userService);
        friendController = new FriendController(friendService);


    }

    @Override
    public Response handleRequest(Request request) throws IOException {
        switch (request.getMethod()) {
            case GET: {
                if (request.getPathname().contains("/users/")) {
                    String username = request.getPathname().split("/")[2];
                    return getUserController().getUserByUsername(username, request.getAuthorizationToken());
                } else if (request.getPathname().contains("/cards")) {
                    String token = request.getAuthorizationToken();
                    return getCardController().getUserCard(getUserController().getSession().get(token));
                } else if (request.getPathname().contains("/deck")) {
                    String format = request.getParams();
                    String token = request.getAuthorizationToken();
                    return deckController.getDeck(getUserController().getSession().get(token), format);
                } else if (request.getPathname().contains("/stats")) {
                    String token = request.getAuthorizationToken();
                    return gameController.getStats(getUserController().getSession().get(token));
                } else if (request.getPathname().contains("/scoreboard")) {
                    String token = request.getAuthorizationToken();
                    return gameController.getScores(getUserController().getSession().get(token));
                } else if (request.getPathname().contains("/tradings")) {
                    String token = request.getAuthorizationToken();
                    return getTradingController().getTradingDeals(getUserController().getSession().get(token));
                } else if (request.getPathname().contains("/friends")) {
                    String token = request.getAuthorizationToken();
                    return getFriendController().getMyFriends(getUserController().getSession().get(token));
                } else if (request.getPathname().contains("/friendrequests")) {
                    String token = request.getAuthorizationToken();
                    return getFriendController().getMyFriendRequest(getUserController().getSession().get(token));
                }
                break;
            }

            case POST: {
                if (request.getPathname().contains("/users")) {
                    return getUserController().register(request.getBody());
                } else if (request.getPathname().contains("/sessions")) {
                    return getUserController().loginUser(request.getBody());
                } else if (request.getPathname().contains("/transactions/packages")) {
                    String token = request.getAuthorizationToken();
                    return getPackageController().buyPackage(getUserController().getSession().get(token));
                } else if (request.getPathname().contains("/packages")) {
                    String token = request.getAuthorizationToken();
                    return getPackageController().createPackage(getUserController().getSession().get(token), request.getBody());
                } else if (request.getPathname().contains("/battles/friend/")) {
                    String token = request.getAuthorizationToken();
                    String friendname = request.getPathname().split("/")[3];
                    return getGameController().battleWithAFriend(getUserController().getSession().get(token), friendname);
                } else if (request.getPathname().contains("/battles")) {
                    String token = request.getAuthorizationToken();
                    return getGameController().battle(getUserController().getSession().get(token));
                } else if (request.getPathname().contains("/tradings/")) {
                    String token = request.getAuthorizationToken();
                    String tradingId = request.getPathname().split("/")[2];
                    String body = request.getBody();
                    return getTradingController().trade(getUserController().getSession().get(token), body, tradingId);
                } else if (request.getPathname().contains("/tradings")) {
                    String token = request.getAuthorizationToken();
                    return getTradingController().createTrading(getUserController().getSession().get(token), request.getBody());
                } else if (request.getPathname().contains("/friends/accept")) {
                    String token = request.getAuthorizationToken();
                    String friendName = request.getPathname().split("/")[3];
                    return getFriendController().acceptFriendRequest(getUserController().getSession().get(token), friendName);
                } else if (request.getPathname().contains("/friends")) {
                    String token = request.getAuthorizationToken();
                    String friendName = request.getPathname().split("/")[2];
                    return getFriendController().sendFriendReuest(getUserController().getSession().get(token), friendName);
                }
                break;
            }

            case PUT: {
                if (request.getPathname().contains("/users")) {

                    String token = request.getAuthorizationToken();
                    String body = request.getBody();
                    String username = request.getPathname().split("/")[2];
                    System.out.println(token);
                    return getUserController().updateUserProfile(token, username, body);

                } else if (request.getPathname().contains("/deck")) {
                    String token = request.getAuthorizationToken();
                    return deckController.configureDeck(getUserController().getSession().get(token), request.getBody());

                }
                break;
            }


            case DELETE: {
                if (request.getPathname().contains("/tradings/")) {
                    String tradingId = request.getPathname().split("/")[2];
                    String token = request.getAuthorizationToken();
                    return tradingController.deleteTrading(userController.getSession().get(token), tradingId);
                }

                break;
            }


            default:
                break;
        }
        return buildNotExistingRoute();
    }


    private Response buildNotExistingRoute() {
        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.TEXT,
                "Not Found"
        );
    }
}
