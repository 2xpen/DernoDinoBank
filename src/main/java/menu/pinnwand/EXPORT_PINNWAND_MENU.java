package menu.pinnwand;

import java.util.Arrays;

public enum EXPORT_PINNWAND_MENU {
    EXPORT("Pinnwandeinträge exportieren",1),
    ZURUECK("Zurück",0);

    private final String text;
    private final int wahlnummer;

    EXPORT_PINNWAND_MENU(String text, int wahlnummer) {
        this.text = text;
        this.wahlnummer = wahlnummer;
    }

    public static void printAll(){
        Arrays.stream(EXPORT_PINNWAND_MENU.values())
                .forEach(e
                                                    ->
                                                        System.out.println(e.wahlnummer+": " +e.text));
    }

    public static EXPORT_PINNWAND_MENU ofWahlnummer(int wahlnummer) {
        return Arrays.stream(EXPORT_PINNWAND_MENU.values())
                .filter(e -> e.wahlnummer == wahlnummer)
                .findFirst()
                .orElse(null);
    }
}