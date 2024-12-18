package data.user;

import data.identifier.UserId;

public class User {
    private final UserId userId;
    private UserName username;

    public User(UserId userId, UserName username) {
        this.userId = userId;
        this.username = username;
    }

    public static User createUser(UserName userName) {
        return new User(
                new UserId(),
                userName
        );
    }

    public UserName getUsername() {
        return username;
    }

    public void setUsername(UserName username) {
        this.username = username;
    }

    public UserId getUserId() {
        return userId;
    }
}