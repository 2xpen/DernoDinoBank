package menu.anmeldung;

import java.util.Arrays;

public enum ANMELDE_MENU_OPTION {
    ANMELDEN("zur Anmeldung", 1),
    ZURUECK("zurück zum Hauptbildschirm", 0);

    private final String text;
    private final int wahlNummer;
    private final Runnable action;

    ANMELDE_MENU_OPTION(String text, int wahlNummer) {
        this.text = text;
        this.wahlNummer = wahlNummer;
        this.action = () -> System.out.println(wahlNummer + ": " + text);
    }

    public static ANMELDE_MENU_OPTION ofWahlNummer(int wahlNummer) {

        return Arrays.stream(ANMELDE_MENU_OPTION.values())
                .filter(o -> o.wahlNummer == wahlNummer)
                .findFirst()
                .orElse(null);

    }

    public static void printAll() {
        for (ANMELDE_MENU_OPTION option : ANMELDE_MENU_OPTION.values()) {
            option.action.run();
        }
    }
}