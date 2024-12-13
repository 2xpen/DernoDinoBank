package data.user;

public class Passwort {

    private final String passwort;

    public Passwort(String passwort) {
        this.passwort = passwort;
    }

    //todo @leo passwort hashen

    @Override
    public String toString() {
        return passwort;
    }
}
