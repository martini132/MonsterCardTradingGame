package src.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Controller.TradingController;
import main.model.Trading;
import main.model.card.SpellCard;
import main.rest.http.HttpStatus;
import main.rest.server.Response;
import main.rest.services.TradingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TradingControllerTest {


    private TradingService tradingService = mock(TradingService.class);
    private TradingController tradingController;
    private ObjectMapper objectMapper = mock(ObjectMapper.class);

    @BeforeEach
    void init() {
        tradingController = new TradingController(tradingService);
    }

    @Test
    void createTrading_SuccessfullyCreated() throws JsonProcessingException {
        when(tradingService.checkIfCardIsLocked(any(String.class), any(String.class))).thenReturn(null);
        when(tradingService.checkIfIdExists(any(String.class))).thenReturn(false);
        when(tradingService.createTrading(any(Trading.class))).thenReturn(true);
        Response response = tradingController.createTrading("some id", "{}");

        assertEquals(HttpStatus.CREATED.getCode(), response.getStatusCode());
        assertEquals("Created", response.getContent());
    }

    @Test
    void deleteTrading_Successfully() {
        when(tradingService.checkIfIdExists(any(String.class))).thenReturn(true);
        when(tradingService.checkIfCardIsLocked(any(String.class), any(String.class))).thenReturn(null);
        when(tradingService.deleteTrade(any(String.class))).thenReturn(true);

        Response response = tradingController.deleteTrading("someId", "someTradingId");

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        assertEquals("Trading was deleted", response.getContent());

    }

    @Test
    void deleteTrading_TradingIdExist_ProvidedDealNotFound(){
        when(tradingService.checkIfIdExists(any(String.class))).thenReturn(false);

        Response response = tradingController.deleteTrading("someUserId","someTradingId");

        assertEquals(HttpStatus.NOT_FOUND.getCode(),response.getStatusCode());
        assertEquals("The provided deal ID was not found.",response.getContent());
    }
}