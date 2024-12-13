package data.geschaeftsvorfall;

import data.anweisungen.UeberweisungsAnweisung;
import data.identifier.KontoId;

public class UeberweisungGevo extends GeschaeftsVorfall {

    private final KontoId empfaengerId;
    private final double betrag;
    private final String beschreibung;


    public UeberweisungGevo(UeberweisungsAnweisung anweisung) {
        super(GevoArt.UEBERWEISUNG, anweisung.getSenderId());
        this.empfaengerId = anweisung.getEmpfaengerId();
        this.betrag = anweisung.getBetrag();
        this.beschreibung = anweisung.getBeschreibung();
    }

    public double getBetrag() {
        return betrag;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public KontoId getEmpfaengerId() {
        return empfaengerId;
    }

}
