package data.konto;

import data.identifier.KontoId;
import data.identifier.UserId;

public class Konto {
    private final KontoId kontoID;
    private final UserId userId;
    private double kontostand;
    
    public Konto(UserId userId) {
        this.kontoID = new KontoId();
        this.userId = userId;
        this.kontostand = 1000;
    }

    public UserId getUserId() {
        return userId;
    }

    public KontoId getKontoID() {
        return kontoID;
    }

    public double getKontostand() {
        return kontostand;
    }

    public void setKontostand(double kontostand) {
        this.kontostand = kontostand;
    }
}