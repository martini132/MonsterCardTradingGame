package main.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import main.model.card.Card;
import main.rest.http.ContentType;
import main.rest.http.HttpStatus;
import main.rest.server.Response;
import main.rest.services.DeckService;

import java.util.List;

public class DeckController extends Controller {
    private DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }


    public Response configureDeck(String userId, String body) throws JsonProcessingException {

        if (body.isEmpty()) {
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.TEXT,
                    "Body missing"
            );
        }

        List<String> cardIds = getObjectMapper().readValue(body, List.class);

        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }

        if (cardIds.size() != 4) {
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.TEXT,
                    "The provided deck did not include the required amount of cards"
            );
        }

        boolean worked = deckService.configureDeck(userId, cardIds);


        String message = "";
        HttpStatus httpStatus = HttpStatus.OK;

        if (worked) {
            message = "The deck has been successfully configured";
        } else {
            message = "At least one of the provided cards does not belong to the user or is not available.";
            httpStatus = HttpStatus.Forbidden;
        }

        return new Response(
                httpStatus,
                ContentType.TEXT,
                message
        );
    }

    public Response getDeck(String userId, String format) throws JsonProcessingException {
        boolean asPlain = false;

        if (format.contains("plain")) {
            asPlain = true;
        }

        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }


        List<Card> card = deckService.getDeck(userId);
        HttpStatus httpStatus = HttpStatus.OK;

        if (card.size() == 0) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        String dataJSON = getObjectMapper().writeValueAsString(card);

        if (asPlain) {
            return new Response(
                    httpStatus,
                    ContentType.TEXT,
                    card.toString()
            );
        } else {
            return new Response(
                    httpStatus,
                    ContentType.JSON,
                    "{ \"data\": " + dataJSON + ", \"error\": null }"
            );
        }
    }
}
