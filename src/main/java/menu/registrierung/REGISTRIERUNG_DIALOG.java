package menu.registrierung;

public enum REGISTRIERUNG_DIALOG {
    NAME_EINGEBEN("Bitte Namen angeben"),
    PASSWORT("Bitte Passwort angeben");

    private final String text;
    private final Runnable action;

    REGISTRIERUNG_DIALOG(String text) {
        this.text = text;
        this.action = () -> System.out.println(text);
    }

    public void print() {
        action.run();
    }
}