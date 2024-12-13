package menu.anmeldung;

import java.util.Arrays;

public enum ANMELDE_DIALOG{

    ANMELDE_FENSTER("Bitte Anmelden"),
    BENUTZERNAMEN_EINGEBEN("geben sie ihren Benutzernamen ein: "),
    PASSWORT_EINGEBEN("geben sie ihr Passwort ein: "),
    PASSWORT_NICHT_KORREKT("Passwort ist nicht korrekt"),
    BENUTZERNAME_NICHT_VERGEBEN("Benutzer Name ist nicht vergeben"),
    ZURÜCK("zurück");


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
