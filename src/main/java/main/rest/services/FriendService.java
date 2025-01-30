package main.rest.services;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.daos.FriendListDao;
import main.daos.FriendRequestDao;
import main.daos.UserDao;
import main.model.FriendRequest;
import main.model.FriendsList;
import main.model.User;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Setter(AccessLevel.PRIVATE)
@Getter
public class FriendService {
    private FriendListDao friendListDao;
    private FriendRequestDao friendRequestDao;
    private UserDao userDao;

    public FriendService(FriendListDao friendListDao, UserDao userDao, FriendRequestDao friendRequestDao) {
        this.friendListDao = friendListDao;
        this.userDao = userDao;
        this.friendRequestDao = friendRequestDao;
    }

    public User checkIfFriendExists(String friendName) {
        try {
            return userDao.read(friendName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

//    public boolean checkIfAllowedToAcceptRequest(String userID, String requestId, St) {
//
//        try {
//            FriendRequest friendRequest = friendRequestDao.read(requestId);
//            if (userID.equals(friendRequest.getUserid())) {
//                return true;
//            }
//
//            return false;
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//
//        return false;
//    }

    public boolean checkIfFriendRquestExists(String userId, String friendName) {
        try {

            User sender = userDao.getById(userId);
            User receiverName = userDao.read(friendName);

            FriendRequest friendRequest = friendRequestDao.getByUserIdAndSender(receiverName.getId(), sender.getUsername());

            System.out.println(friendRequest + " friendrequest");

            if (friendRequest == null) {
                return false;
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


    public FriendRequest getFriendRequestById(String id) {
        try {

            System.out.println(id);
            FriendRequest friendRequest = friendRequestDao.read(id);

            System.out.println(friendRequest);

            return friendRequest;
        } catch (SQLException throwables) {
            return null;
        }
    }


    public boolean createFriendRequest(String friendName, String myUserId) {

        try {
            User sender = userDao.getById(myUserId);
            User receiverName = userDao.read(friendName);

            System.out.println(friendName);
            System.out.println(sender);
            System.out.println(receiverName);

            FriendRequest friendRequest = new FriendRequest(UUID.randomUUID().toString(), receiverName.getId(), sender.getUsername());
            friendRequestDao.create(friendRequest);

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;

        }
    }

    public boolean acceptFriendRequest(String userId, String friendRequestId) {

        try {
            FriendRequest friendRequest = friendRequestDao.read(friendRequestId);
            String username1 = friendRequest.getSender();
            String username2 = userDao.getById(userId).getUsername();
            FriendsList friendsList = new FriendsList(UUID.randomUUID().toString(), username1, username2, "Friends");
            friendListDao.create(friendsList);
            friendRequestDao.delete(friendRequestId);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return false;
    }

    public List<FriendRequest> getMyFriendRequest(String userId) {
        List<FriendRequest> friendRequests = new LinkedList<>();

        try {
            friendRequests = friendRequestDao.getByUserId(userId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return friendRequests;
    }


    public List<FriendsList> getMyFriendList(String userId) {



        List<FriendsList> friendList = new LinkedList<>();

        try {
            User user = userDao.getById(userId);
            friendList = friendListDao.readByUsername(user.getUsername());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return friendList;
    }
}
