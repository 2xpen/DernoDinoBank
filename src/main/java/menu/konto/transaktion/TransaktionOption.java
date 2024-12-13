package menu.konto.transaktion;

import java.util.Arrays;

public enum TransaktionOption {
    EINFACHE_UEBERWEISUNG("Einfache Überweisung", 1),
    MASSEN_UBERWEISUNG("Massenüberweisung ", 2),
    GELD_ABHEBEN("Geld abheben", 3),
    Zurueck("zurück", 0);


    private final String text;
    private final int wahlNummer;
    private final Runnable action;

    TransaktionOption(String text, int wahlNummer) {
        this.text = text;
        this.wahlNummer = wahlNummer;
        this.action = () -> System.out.println(wahlNummer + ": " + text);
    }


    public static TransaktionOption ofWahlNummer(int nummer) {
        return Arrays.stream(TransaktionOption.values())
                .filter(e -> e.wahlNummer == nummer)
                .findFirst()
                .orElse(null);
    }

    public void print() {
        action.run();
    }
}
