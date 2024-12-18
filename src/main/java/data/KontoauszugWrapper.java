package data;

import data.geschaeftsvorfall.KontoauszugZeile;

import java.util.ArrayList;
import java.util.List;

public class KontoauszugWrapper {

    private final List<KontoauszugZeile> kontauszugZeile = new ArrayList<>();

    public KontoauszugWrapper(List<KontoauszugZeile> kontauszugZeile) {
        this.kontauszugZeile.addAll(kontauszugZeile);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Datum").append(" | ").append("Empfänger").append(" | ").append("Sender").append(" | ").append("Beschreibung").append(" | ").append("Betrag").append("\n");

        for (int i = 0; i < kontauszugZeile.size(); i++) {
            sb.append(i + 1)
                    .append(" :")
                    .append(kontauszugZeile.get(i).toString())
                    .append("\n");
        }

        return sb.toString();
    }

    public List<KontoauszugZeile> getKontauszugZeile() {
        return kontauszugZeile;
    }
}
