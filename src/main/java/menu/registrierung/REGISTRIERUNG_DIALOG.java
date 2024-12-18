package menu.registrierung;

public enum REGISTRIERUNG_DIALOG {
    NAME_EINGEBEN("Bitte namen angeben"),
    PASSWORT("Bitte passwort angeben");

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