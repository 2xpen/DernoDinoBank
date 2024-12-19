package data.geschaeftsvorfall;

import java.util.Arrays;

public enum GevoArt {
    ABHEBUNG("Abhebung"),
    UEBERWEISUNG("Ueberweisung"),
    UNBEKANNT("");

    private final String name;

    GevoArt(String name) {
        this.name = name;
    }

    public static GevoArt ermittleGevoAusString(String name) {
        return Arrays.stream(GevoArt.values())
                .filter(g -> g.toString().equals(name))
                .findFirst()
                .orElse(UNBEKANNT);
    }

    public String toString() {
        return name;
    }
}