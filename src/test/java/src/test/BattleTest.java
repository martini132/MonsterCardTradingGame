package src.test;

import main.Battle;
import main.dtos.UserDeckDTO;
import main.model.User;
import main.model.card.Card;
import main.model.card.MonsterCard;
import main.model.card.SpellCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {

    private UserDeckDTO dto1;
    private UserDeckDTO dto2;

    @BeforeEach
    void init() {
        dto1 = new UserDeckDTO(new User("User1", "Test"), new LinkedList<>());
        dto2 = new UserDeckDTO(new User("User2", "Test"), new LinkedList<>());
    }

    private Method getMonsterFightMethod() throws NoSuchMethodException {
        Method method = Battle.class.getDeclaredMethod("monsterFight", UserDeckDTO.class, UserDeckDTO.class, Card.class, Card.class);
        method.setAccessible(true);
        return method;
    }

    private Method getElementFightMethod() throws NoSuchMethodException {
        Method method = Battle.class.getDeclaredMethod("elementFight", UserDeckDTO.class, UserDeckDTO.class, Card.class, Card.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    void monsterFight_FireElvesWinsUser2_Wins() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Card c1 = new MonsterCard("random", "Dragon", 40);
        Card c2 = new MonsterCard("random", "FireElves", 10);

        UserDeckDTO winner = (UserDeckDTO) getMonsterFightMethod().invoke(UserDeckDTO.class, dto1, dto2, c1, c2);
        assertEquals(dto2.getUser().getUsername(), winner.getUser().getUsername());
    }

    @Test
    void elementFight_WaterSpell_Draw() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Card c1 = new SpellCard("random", "WaterSpell", 40);
        Card c2 = new SpellCard("random", "FireSpell", 160);

        UserDeckDTO winner = (UserDeckDTO) getElementFightMethod().invoke(UserDeckDTO.class, dto1, dto2, c1, c2);

        assertNull(winner); //unentschieden
    }

    @Test
    void elementFight_mixedFight_WaterSpell_Wins() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Card c1 = new MonsterCard("random", "Knight", 40);
        Card c2 = new SpellCard("random", "WaterSpell", 10);

        UserDeckDTO winner = (UserDeckDTO) getMonsterFightMethod().invoke(UserDeckDTO.class, dto1, dto2, c1, c2);
        assertEquals(dto2.getUser().getUsername(), winner.getUser().getUsername());
    }

    @Test
    void elementFight_RegularSpellWith40Damage_Wins() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Card c1 = new SpellCard("random", "RegularSpell", 40);
        Card c2 = new SpellCard("random", "RegularSpell", 39);

        UserDeckDTO winner = (UserDeckDTO) getElementFightMethod().invoke(UserDeckDTO.class, dto1, dto2, c1, c2);
        assertEquals(dto1.getUser().getUsername(), winner.getUser().getUsername());
    }

    @Test
    void elementFight_mixedFight_Dragon_Wins() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Card c1 = new MonsterCard("random", "Dragon", 20);
        Card c2 = new SpellCard("random", "WaterSpell", 40);

        UserDeckDTO winner = (UserDeckDTO) getMonsterFightMethod().invoke(UserDeckDTO.class, dto1, dto2, c1, c2);
        assertEquals(dto1.getUser().getUsername(), winner.getUser().getUsername());
    }

}