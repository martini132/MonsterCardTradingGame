package main.rest.services;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.daos.CardDao;
import main.daos.DeckDao;
import main.model.Deck;
import main.model.card.Card;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
public class DeckService {

    private DeckDao deckDao;

    private CardDao cardDao;

    public DeckService(DeckDao deckDao, CardDao cardDao) {
        this.deckDao = deckDao;
        this.cardDao = cardDao;
    }

    public boolean configureDeck(String userId, List<String> cardIds) {
        System.out.println("configureDeck wurde aufgerufen für User: " + userId);

        boolean back = false;
        Deck deck = null;

        try {
            String oldDeckId = deckDao.getDeckIdByUserId(userId);
            System.out.println("Altes Deck gefunden: " + oldDeckId);

            String deckId = oldDeckId;

            if (oldDeckId == null) {
                System.out.println("Kein altes Deck gefunden. Erstelle ein neues Deck...");
                deck = new Deck(UUID.randomUUID().toString(), userId);
                deckId = deck.getDeckId();

                boolean created = deckDao.create(deck);

                if (created) {
                    System.out.println("Neues Deck erstellt mit ID: " + deckId);
                } else {
                    System.out.println("Fehler: Deck konnte nicht erstellt werden!");
                    return false;
                }
            } else {
                System.out.println("Ein altes Deck existiert bereits. Es wird nicht neu erstellt.");
            }

            for (var cardId : cardIds) {
                System.out.println("Versuche Karte " + cardId + " mit Deck " + deckId + " zu verknüpfen...");
                int retunValue = getCardDao().updateDeckID(cardId, deckId, userId);
                System.out.println("SQL-Update für " + cardId + " - Ergebnis: " + retunValue);

                if (retunValue == 0) {
                    System.out.println("Fehler beim Update für Karte " + cardId);
                    back = true;
                    break;
                }
            }

            if (back) {
                System.out.println("Fehler beim Karten-Update, altes Deck wird wiederhergestellt.");
                return false;
            }

            return true;
        } catch (SQLException e) {
            System.out.println("SQL-Fehler in configureDeck: " + e.getMessage());
        }

        return false;
    }


    public List<Card> getDeck(String userId) {
        try {
            String deckId = deckDao.getDeckIdByUserId(userId);

            List<Card> cards = cardDao.getByDeckid(deckId);

            return cards;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
