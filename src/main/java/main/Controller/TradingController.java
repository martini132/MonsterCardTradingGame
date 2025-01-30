package main.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import main.model.Trading;
import main.model.card.Card;
import main.rest.http.ContentType;
import main.rest.http.HttpStatus;
import main.rest.server.Response;
import main.rest.services.TradingService;

import java.util.List;

public class TradingController extends Controller {
    private TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    public Response createTrading(String userId, String body) throws JsonProcessingException {
        if(body.isEmpty()){
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.TEXT,
                    "Body missing"
            );
        }

        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }

        Trading trading = getObjectMapper().readValue(body, Trading.class);

        Card c = tradingService.checkIfCardIsLocked(userId, trading.getCardToTrade());


        if (c != null) {
            return new Response(
                    HttpStatus.Forbidden,
                    ContentType.TEXT,
                    "The deal contains a card that is not owned by the user or locked in the deck."
            );
        }

        boolean exists = tradingService.checkIfIdExists(trading.getId());

        if (exists) {

            return new Response(
                    HttpStatus.Conflict,
                    ContentType.TEXT,
                    "A deal with this deal ID already exists."
            );
        }

        HttpStatus httpStatus = HttpStatus.CREATED;
        String message = "Created";
        if (!tradingService.createTrading(trading)) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Internal Server Error";
        }

        return new Response(
                httpStatus,
                ContentType.TEXT,
                message
        );
    }


    public Response getTradingDeals(String userId) throws JsonProcessingException {
        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }


        List<Trading> tradingList = tradingService.getAllTrades();

        if (tradingList == null || tradingList.size() == 0) {
            return new Response(
                    HttpStatus.NO_CONTENT,
                    ContentType.TEXT,
                    "No Trading deals available"
            );
        }

        String dataJson = getObjectMapper().writeValueAsString(tradingList);

        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                "{ \"data\": " + dataJson + ", \"error\": null }"
        );
    }


    public Response deleteTrading(String userId, String tradingId) {
        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }

        boolean exists = tradingService.checkIfIdExists(tradingId);

        if (!exists) {

            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.TEXT,
                    "The provided deal ID was not found."
            );
        }

        Card c = tradingService.checkIfCardIsLocked(userId, tradingId);

        if (c != null) {
            return new Response(
                    HttpStatus.Forbidden,
                    ContentType.TEXT,
                    "The deal contains a card that is not owned by the user"
            );
        }


        boolean worked = tradingService.deleteTrade(tradingId);

        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Trading was deleted";

        if (!worked) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Internal Error";
        }

        return new Response(
                httpStatus,
                ContentType.TEXT,
                message
        );

    }

    public Response trade(String userId, String myTradingCardId, String tradingId) throws JsonProcessingException {

        if(myTradingCardId.isEmpty()){
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.TEXT,
                    "Body missing"
            );
        }

        myTradingCardId = getObjectMapper().readValue(myTradingCardId, String.class);

        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }


        Card c = tradingService.checkIfCardIsLocked(userId, myTradingCardId);

        if (c != null) {
            return new Response(
                    HttpStatus.Forbidden,
                    ContentType.TEXT,
                    "The deal contains a card that is not owned by the user or locked in the deck."
            );
        }

        boolean exists = tradingService.checkIfIdExists(tradingId);

        if (!exists) {

            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.TEXT,
                    "The provided deal ID was not found."
            );
        }

        boolean worked = tradingService.trade(userId, tradingId, myTradingCardId);

        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Trading was executed";

        if (!worked) {
            httpStatus = HttpStatus.Forbidden;
            message = "Requirements are not met (Type, MinimumDamage) or you tried to trade with yourself";
        }

        return new Response(
                httpStatus,
                ContentType.TEXT,
                message
        );
    }
}
