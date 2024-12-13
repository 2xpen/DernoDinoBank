package repository;

public enum Fehlermeldung_DBConnection {

    CONNECTION_LOST("Verbindung zum Server verloren"),
    CREDENTIALS_INCORRECT("Verbindungsaufbau zum Server fehlgeschlagen ");


    private final String text;
    private final Runnable action;

    Fehlermeldung_DBConnection(String text) {
        this.text = text;
        this.action = () -> System.out.println("Fehlermeldung: " + text);
    }

    public void print(){
        action.run();
    }

}
