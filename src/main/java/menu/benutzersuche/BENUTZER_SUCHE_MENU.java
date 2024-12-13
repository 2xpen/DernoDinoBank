package menu.benutzersuche;

import menu.pinnwand.PINNWAND_MENU_OPTION;

import java.util.Arrays;

public enum BENUTZER_SUCHE_MENU {
    PINNWAND_MENU("Pinnwand", 1),
    DIRECT_MESSAGES_MENU("Direct", 2),
    ZURUECK("Zurück", 0);
    private final String text;
    private final int wahlNummer;
    private final Runnable action;
    BENUTZER_SUCHE_MENU(String text, int wahlNummer){
        this.text = text;
        this.wahlNummer = wahlNummer;
        this.action = () -> System.out.println(wahlNummer + ": " + text);
    }

    public static void printAll() {
        for (BENUTZER_SUCHE_MENU option : BENUTZER_SUCHE_MENU.values()) {
            option.action.run();
        }
    }
    public static BENUTZER_SUCHE_MENU ofWahlNummer(int nummer) {

        return Arrays.stream(BENUTZER_SUCHE_MENU.values())
                .filter(e -> e.wahlNummer == nummer)
                .findFirst()
                .orElse(null);
    }
}
