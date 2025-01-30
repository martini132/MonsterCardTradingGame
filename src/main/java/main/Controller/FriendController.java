package main.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import main.model.FriendRequest;
import main.model.FriendsList;
import main.model.User;
import main.rest.http.ContentType;
import main.rest.http.HttpStatus;
import main.rest.server.Response;
import main.rest.services.FriendService;

import java.util.List;

public class FriendController extends Controller {

    private FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    public Response sendFriendReuest(String userId, String friendname) {
        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }

        User friend = friendService.checkIfFriendExists(friendname);
        if (friend == null || friend.getId().equals(userId)) {
            return new Response(
                    HttpStatus.Forbidden,
                    ContentType.TEXT,
                    "User does not exist or you tried to send a friend request to you"
            );
        }

        if (friendService.checkIfFriendRquestExists(userId, friendname)) {
            return new Response(
                    HttpStatus.Conflict,
                    ContentType.TEXT,
                    "A friend Request was already sent"
            );
        }


        boolean worked = friendService.createFriendRequest(friendname, userId);

        HttpStatus httpStatus = HttpStatus.OK;
        String message = "Friend Request is sent";

        if (!worked) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Server Error";
        }

        return new Response(
                httpStatus,
                ContentType.TEXT,
                message
        );
    }


    public Response acceptFriendRequest(String userId, String friendRequestId) {

        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }

        FriendRequest friendRequest = friendService.getFriendRequestById(friendRequestId);

        if (friendRequest == null) {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.TEXT,
                    "The provied Id was not found"
            );
        }

        if (!friendRequest.getUserid().equals(userId)) {
            return new Response(
                    HttpStatus.Forbidden,
                    ContentType.TEXT,
                    "You are not allowed to accept the request"
            );
        }

        friendService.acceptFriendRequest(userId, friendRequestId);

        return new Response(
                HttpStatus.OK,
                ContentType.TEXT,
                "Friend request accepted"
        );
    }

    public Response getMyFriendRequest(String userId) throws JsonProcessingException {

        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }


        List<FriendRequest> friendRequests = friendService.getMyFriendRequest(userId);

        System.out.println(friendRequests);

        String dataJson = getObjectMapper().writeValueAsString(friendRequests);


        HttpStatus httpStatus = HttpStatus.OK;


        if (friendRequests.size() == 0) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new Response(
                httpStatus,
                ContentType.JSON,
                "{ \"data\": " + dataJson + ", \"error\": null }"
        );
    }


    public Response getMyFriends(String userId) throws JsonProcessingException {

        if (userId == null) {
            return new Response(
                    HttpStatus.Unauthorized,
                    ContentType.TEXT,
                    "Token Missing/Token invalid"
            );
        }


        List<FriendsList> friendsLists = friendService.getMyFriendList(userId);
        String dataJson = getObjectMapper().writeValueAsString(friendsLists);


        HttpStatus httpStatus = HttpStatus.OK;


        if (friendsLists.size() == 0) {
            httpStatus = HttpStatus.NO_CONTENT;
        }


        return new Response(
                httpStatus,
                ContentType.JSON,
                "{ \"data\": " + dataJson + ", \"error\": null }"
        );
    }


}
