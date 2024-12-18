package data.anweisungen;

import data.identifier.KontoId;

public class UpdateAbhebenKontostand {

    private final KontoId kontoId;
    private final double neuerKontostand;

    public UpdateAbhebenKontostand(KontoId kontoId, double neuerKontostand) {
        this.kontoId = kontoId;
        this.neuerKontostand = neuerKontostand;
    }

    public KontoId getKontoId() {
        return kontoId;
    }

    public double getNeuerKontostand() {
        return neuerKontostand;
    }
}
