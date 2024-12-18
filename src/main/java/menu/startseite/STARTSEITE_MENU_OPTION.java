package menu.startseite;

import java.util.Arrays;

public enum STARTSEITE_MENU_OPTION {
    REGISTRIEREN("Registrieren", 1),
    ANMELDEN("Anmelden ", 2),
    PROGRAMM_SCHLIESSEN("Schliessen", 0);

    private final String text;
    private final int wahlNummer;
    private final Runnable action;

    STARTSEITE_MENU_OPTION(String text, int wahlNummer) {
        this.text = text;
        this.wahlNummer = wahlNummer;
        this.action = () -> System.out.println(wahlNummer + ": " + text);
    }

    public static STARTSEITE_MENU_OPTION ofWahlNummer(int nummer) {
        return Arrays.stream(STARTSEITE_MENU_OPTION.values())
                .filter(e -> e.wahlNummer == nummer)
                .findFirst()
                .orElse(null);
    }

    public static void printAll() {
        for (STARTSEITE_MENU_OPTION option : STARTSEITE_MENU_OPTION.values()) {
            option.action.run();
        }
    }

    public String getText() {
        return text;
    }

    public void print() {
        action.run();
    }
}

