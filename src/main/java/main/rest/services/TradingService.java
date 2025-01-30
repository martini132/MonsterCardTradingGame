package main.rest.services;

import main.daos.CardDao;
import main.daos.TradingDao;
import main.model.Trading;
import main.model.card.Card;

import java.sql.SQLException;
import java.util.List;

public class TradingService {

    private TradingDao tradingDao;
    private CardDao cardDao;

    public TradingService(TradingDao tradingDao, CardDao cardDao) {
        this.tradingDao = tradingDao;

        this.cardDao = cardDao;
    }


    public Card checkIfCardIsLocked(String userId, String cardId) {
        try {
            return cardDao.getCardsByUserIdAndCheckIfLocked(userId, cardId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public boolean checkIfIdExists(String tradingId) {

        try {
            Trading t = tradingDao.read(tradingId);
            if (t == null)
                return false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean createTrading(Trading trading) {

        try {


            tradingDao.create(trading);
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(throwables.getMessage());
            return false;
        }
    }

    public List<Trading> getAllTrades() {

        try {
            return tradingDao.getAll();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;

    }

    public boolean deleteTrade(String tradingId) {
        try {
            tradingDao.delete(tradingId);
            return true;
        } catch (SQLException throwables) {
            return false;
        }

    }

    public boolean trade(String userId, String tradingId, String myCardId) {

        try {
            Card myCard = cardDao.read(myCardId);
            Trading trade = tradingDao.read(tradingId);
            Card cardToTrade = cardDao.read(trade.getCardToTrade());

            if (cardToTrade.getUserId().equals(userId) || myCard.getDamage() < trade.getMinimumDamage()
                    || myCard.getClass().getSimpleName().contains(trade.getType())) {
                return false;
            }

            myCard.changeUserId(cardToTrade.getUserId());
            cardToTrade.changeUserId(userId);
            deleteTrade(tradingId);
            return true;
        } catch (SQLException throwables) {
;
            System.out.println(throwables.getMessage());
            return false;
        }
    }


}
