package main.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.daos.PackageDao;
import main.model.User;
import main.model.card.Card;
import main.model.card.MonsterCard;
import main.model.card.SpellCard;
import main.rest.http.ContentType;
import main.rest.http.HttpStatus;
import main.rest.server.Response;
import main.rest.services.PackageService;

import java.util.LinkedList;
import java.util.List;

@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PUBLIC)
public class PackageController extends Controller {
    private PackageDao packageDao;
    private PackageService packageService;


    public PackageController(PackageService packageService) {
        setPackageService(packageService);
    }


    public Response createPackage(String uid, String body) throws JsonProcessingException {
        if(body.isEmpty()){
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.TEXT,
                    "Body missing"
            );
        }

        if (uid == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }

        User user = packageService.getUserById(uid);

        System.out.println(user);
        if (user == null || !user.getUsername().equals("admin")) {
            return new Response(
                    HttpStatus.Forbidden,
                    ContentType.TEXT,
                    "Provided user is not admin"
            );
        }

        List<Card> cards = new LinkedList<>();
        JsonNode actualObj = getObjectMapper().readTree(body);
        Card c;

        if (actualObj.isArray()) {
            for (var jsonObj : actualObj) {

                if (jsonObj.get("Name").asText().contains("Spell")) {
                    c = getObjectMapper().readValue(jsonObj.toString(), SpellCard.class);

                } else {
                    c = getObjectMapper().readValue(jsonObj.toString(), MonsterCard.class);
                }

                cards.add(c);
            }
        }


        String responseValue = getPackageService().createPackagesAndCards(cards);
        HttpStatus status = HttpStatus.CREATED;
        String message = "Package and Cards created";

        if (responseValue.equals("23505")) {
            status = HttpStatus.Conflict;
            message = "At least one card in the packages already exists";
        }

        return new Response(
                status,
                ContentType.TEXT,
                message
        );

    }


    public Response buyPackage(String userId) throws JsonProcessingException {

        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }

        if (!getPackageService().checkForPackage()) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.TEXT,
                    "No packages for buying available"
            );
        }

        if (!getPackageService().checkUserMoney(userId)) {
            return new Response(
                    HttpStatus.Forbidden,
                    ContentType.TEXT,
                    "Not enough money for buying"
            );
        }

        List<Card> cards = getPackageService().acquirePackage(userId);

        String dataJson = getObjectMapper().writeValueAsString(cards);
        HttpStatus httpStatus = HttpStatus.OK;
        if (cards == null) {
            httpStatus = HttpStatus.NOT_FOUND;
        }

        return new Response(
                httpStatus,
                ContentType.JSON,
                "{ \"data\": " + dataJson + ", \"error\": null }"
        );
    }


}
