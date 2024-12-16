package menu.konto;

import menu.directMessages.DIRECT_MESSAGES_MENU_OPTION;

import java.util.Arrays;

public enum KONTO_MENU_OPTION {

    OPTION_TRANSAKTION("Transaktion", 1),
    OPTION_KONTOSTAND_ANSEHEN("Kontostand ansehen", 2),
    OPTION_PINNWAND("Pinnwand", 3),
    OPTION_DIRECTMESSAGE("Direktnachrichten", 4),
    OPTION_KONTOBEWEGUNGEN("Kontobewegungen", 5),
    OPTION_PERSONSUCHE("Personensuche",6),
    AUSLOGGEN("ausloggen", 0);


    private final String text;
    private final int wahlNummer;
    private final Runnable action;

    KONTO_MENU_OPTION(String text, int wahlNummer) {
        this.text = text;
        this.wahlNummer = wahlNummer;
        this.action = () -> System.out.println(wahlNummer + ": " + text);
    }

    public static KONTO_MENU_OPTION ofWahlNummer(int nummer) {
        return Arrays.stream(KONTO_MENU_OPTION.values())
                .filter(e -> e.wahlNummer == nummer)
                .findFirst()
                .orElse(null);
    }

    public static void printAll() {
        for (KONTO_MENU_OPTION option : KONTO_MENU_OPTION.values()) {
            option.action.run();
        }
    }


    public String getText() {
        return text;
    }
}
