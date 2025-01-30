package main.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.model.User;
import main.model.card.Card;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter(AccessLevel.PRIVATE)
public class UserDeckDTO {
    private User user;
    private List<Card> deck = new LinkedList<>();

    public UserDeckDTO(User user, List<Card> deck) {
        this.user = user;
        this.deck = deck;
    }
}
