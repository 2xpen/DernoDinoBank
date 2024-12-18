package data.geschaeftsvorfall;

import data.identifier.KontoId;

import java.sql.Timestamp;
import java.util.Optional;

public class GevoZeile {
    private final Timestamp datum;
    private final Optional<KontoId> empfaenger;
    private final KontoId sender;
    private final String beschreibung;
    private final double betrag;
    private final GevoArt art;

    public GevoZeile(Timestamp datum, KontoId empfaenger, KontoId sender, String beschreibung, double betrag, GevoArt art) {
        this.datum = datum;
        this.empfaenger = Optional.ofNullable(empfaenger);
        this.sender = sender;
        this.beschreibung = beschreibung;
        this.betrag = betrag;
        this.art = art;
    }

    public double getBetrag() {
        return betrag;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public Optional<KontoId> getEmpfaenger() {
        return empfaenger;
    }

    public KontoId getSender() {
        return sender;
    }

    public Timestamp getDatum() {
        return datum;
    }

    public GevoArt getArt() {
        return art;
    }

    @Override
    public String toString() {
        return "GevoZeile{" +
                "datum=" + datum +
                ", empfaenger=" + empfaenger +
                ", sender=" + sender +
                ", beschreibung='" + beschreibung + '\'' +
                ", betrag=" + betrag +
                '}';
    }
}