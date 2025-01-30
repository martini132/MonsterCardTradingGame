package src.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Controller.DeckController;
import main.model.card.Card;
import main.model.card.MonsterCard;
import main.model.card.SpellCard;
import main.rest.http.HttpStatus;
import main.rest.server.Response;
import main.rest.services.DeckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeckControllerTest {

    private DeckService deckService = mock(DeckService.class);

    private DeckController deckController;

    @BeforeEach
    void init() {
        deckController = new DeckController(deckService);
    }

    @Test
    void configureDeck_WrongDeckSize() throws JsonProcessingException {

        String body = "[\"2272ba48-6662-404d-a9a1-41a9bed316d9\", \"3871d45b-b630-4a0d-8bc6-a5fc56b6a043\"," +
                "\"166c1fd5-4dcb-41a8-91cb-f45dcd57cef3\"]";

        Response response = deckController.configureDeck("someid", body);

        assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.getStatusCode());
        assertEquals("The provided deck did not include the required amount of cards", response.getContent());
    }


    @Test
    void getDeck_Successfully() throws JsonProcessingException {
        List<Card> cards = new LinkedList<>();
        cards.add(new SpellCard("someId", "WaterSpell", 40));
        cards.add(new SpellCard("someId", "FireSpell", 40));
        cards.add(new SpellCard("someId", "RegularSpell", 40));
        cards.add(new MonsterCard("someId", "Dragon", 40));

        when(deckService.getDeck(any(String.class))).thenReturn(cards);

        String dataJSON = new ObjectMapper().writeValueAsString(cards);


        Response response = deckController.getDeck("someid","");

        String s = "{ \"data\": " + dataJSON + ", \"error\": null }";

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        assertEquals(s, response.getContent());

    }
}