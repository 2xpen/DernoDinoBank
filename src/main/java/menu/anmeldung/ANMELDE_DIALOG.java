package menu.anmeldung;

public enum ANMELDE_DIALOG{
    ANMELDE_FENSTER("Bitte Anmelden"),
    BENUTZERNAMEN_EINGEBEN("Geben Sie ihren Benutzernamen ein: "),
    PASSWORT_EINGEBEN("Geben Sie ihr Passwort ein: "),
    PASSWORT_NICHT_KORREKT("Passwort ist nicht korrekt"),
    BENUTZERNAME_NICHT_VERGEBEN("Benutzer Name ist nicht vergeben"),
    ZURÜCK("Zurück");

    private final String text;
    private final Runnable runnable;

    ANMELDE_DIALOG(String text) {
        this.text = text;
        this.runnable =() -> System.out.println(this.text);
    }

    public void print(){
        this.runnable.run();
    }

    public String getText() {
        return text;
    }
}