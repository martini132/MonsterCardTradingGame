package src.test;

import main.Controller.FriendController;
import main.model.FriendRequest;
import main.model.User;
import main.rest.http.HttpStatus;
import main.rest.server.Response;
import main.rest.services.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FriendControllerTest {

    private FriendService friendService = mock(FriendService.class);
    private FriendController friendController;

    @BeforeEach
    void init() {
        friendController = new FriendController(friendService);
    }


    @Test
    void sendFriendReuest_UserDoesNotExist_ReturForbidden() {

        when(friendService.checkIfFriendExists(any(String.class))).thenReturn(null);

        Response response = friendController.sendFriendReuest("someId", "josef");
        assertEquals(HttpStatus.Forbidden.getCode(), response.getStatusCode());
        assertEquals("User does not exist or you tried to send a friend request to you", response.getContent());

    }


    @Test
    void sendFriendReuest_ToYourself_ReturnForbidden() {

        //User(String username, int coins, String password, String id
        when(friendService.checkIfFriendExists(any(String.class))).thenReturn(new User("josef", 5, "test", "someId"));
        Response response = friendController.sendFriendReuest("someId", "josef");

        assertEquals(HttpStatus.Forbidden.getCode(), response.getStatusCode());
        assertEquals("User does not exist or you tried to send a friend request to you", response.getContent());

    }

    @Test
    void sendFriendRequest_CreatedRequest() {
        when(friendService.checkIfFriendExists(any(String.class))).thenReturn(new User("altenhof", 5, "test", "otherid"));
        when(friendService.checkIfFriendRquestExists(any(String.class), any(String.class))).thenReturn(false);
        when(friendService.createFriendRequest(any(String.class), any(String.class))).thenReturn(true);

        Response response = friendController.sendFriendReuest("someId", "josef");

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        assertEquals("Friend Request is sent", response.getContent());

    }

    @Test
    void acceptFriendRequest_accepted() {


        //String id, String userid, String sender)
        when(friendService.getFriendRequestById(any(String.class))).thenReturn(new FriendRequest("someId", "someId", "someSender"));
        when(friendService.createFriendRequest(any(String.class), any(String.class))).thenReturn(true);

        Response response = friendController.acceptFriendRequest("someId", "someId");


        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        assertEquals("Friend request accepted", response.getContent());

    }
}