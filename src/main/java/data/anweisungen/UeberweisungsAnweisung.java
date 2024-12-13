package data.anweisungen;

import data.identifier.KontoId;

public class UeberweisungsAnweisung extends AnweisungBase {
    private final KontoId senderId;
    private final KontoId empfaengerId;
    private final String beschreibung;
    private final double betrag;

    public UeberweisungsAnweisung(KontoId senderId, KontoId empfaengerId, String beschreibung, double betrag) {
        super();
        this.senderId = senderId;
        this.empfaengerId = empfaengerId;
        this.beschreibung = beschreibung;
        this.betrag = betrag;
    }


    public KontoId getSenderId() {
        return senderId;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public KontoId getEmpfaengerId() {
        return empfaengerId;
    }

    public double getBetrag() {
        return betrag;
    }
}
