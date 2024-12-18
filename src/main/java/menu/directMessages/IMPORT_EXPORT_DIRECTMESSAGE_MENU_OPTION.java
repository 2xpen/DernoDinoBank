package menu.directMessages;

import java.util.Arrays;

public enum IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION {
    EXPORT("Nachricht/en exportieren", 1),
    ZURUECK("Zurück", 0);
    private final String text;
    private final int wahlnummer;
    private final Runnable action;

    IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION(String text, int wahlnummer) {
        this.text = text;
        this.wahlnummer = wahlnummer;
        this.action = () -> System.out.println(wahlnummer + ": " + text);
    }

    public static void printAll() {
        for (IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION option : IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION.values()) {
            option.action.run();
        }
    }

    public static IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION ofWahlNummer(int nummer) {

        return Arrays.stream(IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION.values())
                .filter(e -> e.wahlnummer == nummer)
                .findFirst()
                .orElse(null);
    }

    public String getText() {
        return text;
    }
}