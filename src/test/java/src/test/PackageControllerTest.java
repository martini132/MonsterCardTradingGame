package src.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Controller.PackageController;
import main.model.User;
import main.model.card.Card;
import main.model.card.MonsterCard;
import main.model.card.SpellCard;
import main.rest.http.HttpStatus;
import main.rest.server.Response;
import main.rest.services.PackageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

class PackageControllerTest {

    private PackageService packageService = Mockito.mock(PackageService.class);
    private PackageController packageController;

    @BeforeEach
    void init() {
        packageController = new PackageController(packageService);
    }

    @Test
    void createPackage_UserIsNotAdmin() throws JsonProcessingException {
        when(packageService.getUserById(any(String.class))).thenReturn(new User("altenhof", "1234"));
        Response response = packageController.createPackage("Some uid", "{}");

        assertEquals(HttpStatus.Forbidden.getCode(), response.getStatusCode());
        assertEquals("Provided user is not admin", response.getContent());
    }


    @Test
    void buyPackage_AuthToken_isMissing() throws JsonProcessingException {

        Response response = packageController.buyPackage(null);
        assertEquals(HttpStatus.Unauthorized.getCode(), response.getStatusCode());
        assertEquals("Token Missing/Token invalid", response.getContent());
    }


    @Test
    void buyPackage_NoPackagesAvailable_ToBuy() throws JsonProcessingException {

        when(packageService.checkForPackage()).thenReturn(false);
        Response response = packageController.buyPackage("some id");

        assertEquals(HttpStatus.NOT_FOUND.getCode(), response.getStatusCode());
        assertEquals("No packages for buying available", response.getContent());
    }

    @Test
    void buyPackage_SuccessfullyBought() throws JsonProcessingException {

        //@JsonProperty("Id") String id, @JsonProperty("Name") String name, @JsonProperty("Damage"

        List<Card> cards = new LinkedList<>();
        cards.add(new SpellCard("someId", "WaterSpell", 40));
        cards.add(new SpellCard("someId", "FireSpell", 40));
        cards.add(new SpellCard("someId", "RegularSpell", 40));
        cards.add(new MonsterCard("someId", "Dragon", 40));
        cards.add(new MonsterCard("someId", "Ork", 40));


        when(packageService.checkForPackage()).thenReturn(true);
        when(packageService.checkUserMoney(any(String.class))).thenReturn(true);
        when(packageService.acquirePackage(any(String.class))).thenReturn(cards);

        String dataJson = new ObjectMapper().writeValueAsString(cards);
        Response response = packageController.buyPackage("some id");

        String s = "{ \"data\": " + dataJson + ", \"error\": null }";

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        assertEquals(s, response.getContent());
    }
}