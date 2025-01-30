package main.daos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.Element;
import main.model.card.Card;
import main.model.card.MonsterCard;
import main.model.card.SpellCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException
        ;
import java.util.LinkedList;
import java.util.List;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class CardDao implements DAO<Card> {

    private Connection connection;

    public CardDao(Connection connection) {
        setConnection(connection);
    }

    @Override
    public boolean create(Card card) throws SQLException {
        String query = "INSERT INTO card (cardid,name,damage,typ,weakness,typeweakness,nameandtype,packageid) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, card.getId());
        stmt.setString(2, card.getName());
        stmt.setInt(3, card.getDamage());
        stmt.setString(4, card.getType().toString());
        stmt.setString(5, card.getWeakness());
        stmt.setString(6, card.getTypeWeakness().toString());
        stmt.setString(7, card.getNameAndType());
        stmt.setString(8, card.getPackageId());
        return stmt.execute();
    }

    @Override
    public List<Card> getAll() throws SQLException {

        List<Card> cards = new LinkedList<>();
        String query = "select * from card ";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            cards.add(createCard(rs));
        }

        return cards;
    }


    @Override
    public Card read(String cardId) throws SQLException {
        String query = "select * from card where cardid = ? ";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, cardId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            return createCard(rs);
        }

        return null;
    }

    @Override
    public void update(Card card) {
    }

    public List<Card> getCardsOfSpecificPackage(String packageId) throws SQLException {
        String query = "select cardid, name, damage, typ, weakness,typeweakness,nameandtype,Card.packageid, userid from Card where Card.packageid = ?";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, packageId);
        ResultSet rs = stmt.executeQuery();
        List<Card> cards = new LinkedList<>();

        while (rs.next()) {

            cards.add(createCard(rs));
        }
        return cards;
    }

    public boolean updatePackageId(String packageId, String cardId) throws SQLException {
        String query = "UPDATE card SET packageid = ? WHERE cardid = ?";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, packageId);
        stmt.setString(2, cardId);
        return stmt.execute();

    }

    public boolean updateUserId(String cardId, String userId) throws SQLException {
        String query = "UPDATE card SET userid = ? WHERE cardid = ?";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, userId);
        stmt.setString(2, cardId);
        return stmt.execute();
    }


    public int updateDeckID(String cardId, String deckId, String userId) throws SQLException {
        String query = "UPDATE card SET deckid = ? WHERE cardid = ? and ( userid = ? or userid = null)";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, deckId);
        stmt.setString(2, cardId);
        stmt.setString(3, userId);
        return stmt.executeUpdate();
    }

    public int updateDeckIdByCardId(String cardId, String newDeckId) throws SQLException {
        String query = "UPDATE card SET deckid = ? WHERE cardid = ?";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(2, cardId);
        stmt.setString(1, newDeckId);

        return stmt.executeUpdate();
    }


    @Override
    public void delete(String id) throws SQLException {
        String query = "DELETE FROM card WHERE cardid = ?";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, id);
        stmt.execute();
    }

    public List<Card> getByDeckid(String deckId) throws SQLException {
        List<Card> cards = new LinkedList<>();
        String query = "select * from card where deckid = ?";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, deckId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            cards.add(createCard(rs));
        }

        return cards;
    }

    public List<Card> getByUserID(String userID) throws SQLException {
        List<Card> cards = new LinkedList<>();
        String query = "select * from card where userid = ?";
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, userID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            cards.add(createCard(rs));
        }

        return cards;
    }

    public Card getCardsByUserIdAndCheckIfLocked(String userId, String cardId) throws SQLException {
        String query = "select * from card where userid = ? and cardid = ? and deckid = null";

        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setString(1, userId);
        stmt.setString(2, cardId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            return createCard(rs);
        }

        return null;
    }


    private Card createCard(ResultSet rs) throws SQLException {
        Card c;
        String cardId = rs.getString(1);
        String name = rs.getString(2);
        int damage = rs.getInt(3);
        Element type = Element.valueOf(rs.getString(4));
        String weakness = rs.getString(5);
        Element typeWeakness = Element.valueOf(rs.getString(6));
        String nameAndType = rs.getString(7);
        String packageId = rs.getString(8);
        String userID = rs.getString(9);

        if (name.contains("Spell")) {
            c = new SpellCard(type, name, damage, weakness, typeWeakness, cardId, nameAndType, packageId, userID);
        } else {
            c = new MonsterCard(type, name, damage, weakness, typeWeakness, cardId, nameAndType, packageId, userID);
        }
        return c;
    }

}
