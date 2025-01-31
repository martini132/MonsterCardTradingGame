package src.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import main.Controller.UserController;
import main.daos.GameDao;
import main.daos.UserDao;
import main.model.User;
import main.rest.http.ContentType;
import main.rest.http.HttpStatus;
import main.rest.server.Response;
import main.rest.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;

import org.mockito.junit.MockitoJUnitRunner;



import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
class UserControllerTest {

    private UserService userService = mock(UserService.class);


    @Test
    void register() throws  JsonProcessingException {

        when(userService.createUser(any(User.class))).thenReturn(true);
        UserController userController = new UserController(userService);

        String body = "{\n" +
                "  \"Username\": \"admin\",\n" +
                "  \"Password\": \"test\"\n" +
                "}";

        Response response = userController.register(body);
        Assertions.assertEquals(HttpStatus.CREATED.getCode(), response.getStatusCode());
        Assertions.assertEquals("User Successfully created", response.getContent());


    }

    @Test
    void registerWithError() throws  JsonProcessingException {

        when(userService.createUser(any(User.class))).thenReturn(false);
        UserController userController = new UserController(userService);

        String body = "{\n" +
                "  \"Username\": \"admin\",\n" +
                "  \"Password\": \"test\"\n" +
                "}";

        String errorMessage = "This user already exists";
        String responseMessage = "{ \"error\": " + errorMessage + ", \"data\": null }";

        Response response = userController.register(body);
        Assertions.assertEquals(HttpStatus.Conflict.getCode(), response.getStatusCode());
        Assertions.assertEquals(responseMessage, response.getContent());
        //already registered



    }

}