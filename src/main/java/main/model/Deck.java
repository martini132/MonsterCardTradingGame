package main.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public class Deck {
    @JsonAlias({"userId"})
    private String userId;
    @JsonAlias({"deckId"})
    private String deckId;

    public Deck() {

    }

    public Deck(String deckId, String userId) {
        setDeckId(deckId);
        setUserId(userId);
    }
}
