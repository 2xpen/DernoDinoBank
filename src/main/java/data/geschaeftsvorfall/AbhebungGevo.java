package data.geschaeftsvorfall;

import data.anweisungen.AbhebungsAnweisung;
import data.identifier.GevoId;
import data.identifier.KontoId;
import data.identifier.UserId;

public class AbhebungGevo extends GeschaeftsVorfall {
    private final double betrag;
    private final String beschreibung = "abhebung";

    public AbhebungGevo(AbhebungsAnweisung anweisung) {
        super(GevoArt.ABHEBUNG, anweisung.getKontoId());
        this.betrag = anweisung.getBetrag();
    }

    public double getBetrag() {
        return betrag;
    }

    public String getBeschreibung() {
        return beschreibung;
    }
}