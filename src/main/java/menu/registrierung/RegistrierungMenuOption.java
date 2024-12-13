package menu.registrierung;

import java.util.Arrays;

public enum RegistrierungMenuOption {

    REGISTRIEREN("Registrierung starten", 1),
    ZURUECK("Zurück zum Startmenü", 0);

    private final String text;
    private final int wahlnummer;
    private final Runnable action;

    RegistrierungMenuOption(String text, int wahlnummer) {
        this.text = text;
        this.wahlnummer = wahlnummer;
        this.action = () -> System.out.println(wahlnummer + ": " + text);
    }

    public static RegistrierungMenuOption ofWahlnummer(int wahlnummer) {
        return Arrays.stream(RegistrierungMenuOption.values())
                .filter(o -> o.getWahlnummer() == wahlnummer)
                .findFirst()
                .orElse(null);
    }

    public static void printAll() {
        for (RegistrierungMenuOption o : RegistrierungMenuOption.values()) {
            o.action.run();
        }
    }

    public int getWahlnummer() {
        return wahlnummer;
    }


}
