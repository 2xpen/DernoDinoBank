package menu.pinnwand;

import menu.directMessages.DIRECT_MESSAGES_MENU_OPTION;

import java.util.Arrays;

public enum PINNWAND_KOMMENTAR_MENU {
    KOMMENTAR_SCHREIBEN("Wenn du einen Kommentar schreiben möchtest Bitte 1 eingeben", 1),
    ZURUECK("Zurück", 0);

    private final String text;
    private final int wahlNummer;
    private final Runnable action;
    PINNWAND_KOMMENTAR_MENU(String text, int wahlNummer){
        this.text = text;
        this.wahlNummer = wahlNummer;
        this.action = () -> System.out.println(wahlNummer + ": " + text);
    }
    public static void printAll() {
        for (PINNWAND_KOMMENTAR_MENU option : PINNWAND_KOMMENTAR_MENU.values()) {
            option.action.run();
        }
    }
    public static PINNWAND_KOMMENTAR_MENU ofWahlNummer(int nummer) {

        return Arrays.stream(PINNWAND_KOMMENTAR_MENU.values())
                .filter(e -> e.wahlNummer == nummer)
                .findFirst()
                .orElse(null);
    }
    public String getText() {
        return text;
    }
}
