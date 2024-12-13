package data;

import data.geschaeftsvorfall.KontoauszugZeile;

import java.util.ArrayList;
import java.util.List;

public class KontoauszugWrapper {


    //der hier hat quasi nur die toString methode, solllkte vielleicht auch die sortier funktionen habe zb gevo art ob das eine abhebung war oder eine uerbeweisung

    private final List<KontoauszugZeile> kontauszugZeile = new ArrayList<>();

    public KontoauszugWrapper(List<KontoauszugZeile> kontauszugZeile) {
        this.kontauszugZeile.addAll(kontauszugZeile);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        //todo einen festen header String einführen
        sb.append("DATUM").append(" | ").append("Empfänger").append(" | ").append("Sender").append(" | ").append("Beschreibung").append(" | ").append("Betrag").append("\n");

        for (int i = 0; i < kontauszugZeile.size(); i++) {
            sb.append(i + 1)
                    .append(" :")
                    .append(kontauszugZeile.get(i).toString())
                    .append("\n");
        }

        return sb.toString();
    }


}
