package data.anweisungen;

import data.identifier.KontoId;

public class AbhebungsAnweisung extends AnweisungBase {

    private final KontoId kontoId;
    private final double betrag;

    public AbhebungsAnweisung(KontoId kontoId, double betrag) {
        super();
        this.kontoId = kontoId;
        this.betrag = betrag;
    }

    public KontoId getKontoId() {
        return kontoId;
    }

    public double getBetrag() {
        return betrag;
    }

}
