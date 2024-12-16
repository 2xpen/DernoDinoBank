package menu.pinnwand;

import menu.konto.KONTO_MENU_OPTION;

import java.util.Arrays;

public enum PINNWAND_MENU_OPTION {

    PINNWAND_ANSEHEN("eigene Pinnwand ansehen", 1),
    ZURUECK("zurück", 0);

    private final String text;
    private final int wahlNummer;
    private final Runnable action;

    PINNWAND_MENU_OPTION(String text, int wahlNummer) {
        this.text = text;
        this.wahlNummer = wahlNummer;
        this.action = () -> System.out.println(wahlNummer + ": " + text);
    }

    public static void printAll() {
        for (PINNWAND_MENU_OPTION option : PINNWAND_MENU_OPTION.values()) {
            option.action.run();
        }
    }

    public static PINNWAND_MENU_OPTION ofWahlNummer(int nummer) {

        return Arrays.stream(PINNWAND_MENU_OPTION.values())
                .filter(e -> e.wahlNummer == nummer)
                .findFirst()
                .orElse(null);
    }

    public String getText() {
        return text;
    }
}
