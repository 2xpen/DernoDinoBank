package data.user;

public class UserName {
    private final String name;

    public UserName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}