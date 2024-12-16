package menu.directMessages;

import menu.pinnwand.PINNWAND_MENU_OPTION;

import java.util.Arrays;

public enum DIRECT_MESSAGES_MENU_OPTION {
    DIRECT_MESSAGES_ANSEHEN("Alle Direktnahrichten ansehen", 1),
    ZURÜCK("zurück", 0);
    private final String text;
    private final int wahlnummer;
    private final Runnable action;

    DIRECT_MESSAGES_MENU_OPTION(String text, int wahlnummer) {
        this.text = text;
        this.wahlnummer = wahlnummer;
        this.action = () -> System.out.println(wahlnummer + ": " + text);

    }


    public static void printAll() {
        for (DIRECT_MESSAGES_MENU_OPTION option : DIRECT_MESSAGES_MENU_OPTION.values()) {
            option.action.run();
        }
    }


    public static DIRECT_MESSAGES_MENU_OPTION ofWahlNummer(int nummer) {

        return Arrays.stream(DIRECT_MESSAGES_MENU_OPTION.values())
                .filter(e -> e.wahlnummer == nummer)
                .findFirst()
                .orElse(null);
}

    public String getText() {
        return text;
    }
}
