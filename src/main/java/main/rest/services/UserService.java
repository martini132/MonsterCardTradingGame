package main.rest.services;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import main.daos.GameDao;
import main.daos.UserDao;
import main.daos.UserProfileDao;
import main.model.Statistik;
import main.model.User;
import main.model.UserProfile;

import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
public class UserService {
    private UserDao userDao;
    private GameDao gameDao;
    private UserProfileDao userProfileDao;

    public UserService(UserDao userDao, GameDao gameDao, UserProfileDao userProfileDao) {
        this.userDao = userDao;
        this.gameDao = gameDao;
        this.userProfileDao = userProfileDao;
    }

    public boolean createUser(User user) {

        System.out.println(user);
        try {
            userDao.create(user);
            gameDao.create(new Statistik(user.getUsername(), 100, 0, 0, user.getId(), UUID.randomUUID().toString(), 0,0));
            System.out.println(userProfileDao);
            userProfileDao.create(new UserProfile(UUID.randomUUID().toString(), user.getUsername(), "", "", user.getId()));
            return true;
        } catch (SQLException throwables) {
            System.out.println(throwables);
            return false;
        }
    }


    public User login(String username) {
        try {
            return userDao.read(username);
        } catch (SQLException throwables) {
            return null;
        }
    }


    public User getByUsername(String username) {
        try {
            return userDao.read(username);
        } catch (SQLException throwables) {
            return null;
        }
    }


    public boolean updateUserProfile(String userId, String name, String bio, String image) {

        try {
            userProfileDao.updateUserProfileByUserId(userId, name, bio, image);
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

}
