package data.geschaeftsvorfall;

import data.user.UserName;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Optional;

public class KontoauszugZeile {

//die klasse klasse ist nur zum angucken, quasi das view objekt von Kontauszugszeile


    private final Timestamp datum;
    private final Optional<UserName> empfaenger;
    private final UserName sender;
    private final String beschreibung;
    private final GevoArt art;
    private final double betrag;

    public KontoauszugZeile(Timestamp datum, Optional<UserName> empfaenger, UserName sender, String beschreibung, double betrag, GevoArt art) {
        this.datum = datum;
        this.empfaenger = empfaenger;
        this.sender = sender;
        this.beschreibung = beschreibung;
        this.art = art;
        this.betrag = betrag;
    }

    @Override
    public String toString() {
        // ob das so geil ist mit der zeitzone definieren?
        //todo @tom mit Numberformat gucken das son double immer mit zwei nachkommastellen anzeigen
        return
                datum.toInstant()
                        .atZone(ZoneId.systemDefault()) // Zeitzone definieren
                        .toLocalDate() + " | "
                        + empfaenger.toString() + " | "
                        + sender.toString() + " | "
                        + beschreibung + " | "
                        + betrag + "€";
    }

    public String getDatum() {
        return datum.toString();
    }

    public String getEmpfaenger() {
        if (empfaenger.isPresent()) {
            return empfaenger.get().toString();
        }
        return "";

    }

    public String getSender() {
        return sender.toString();
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public GevoArt getArt() {
        return art;
    }

    public double getBetrag() {
        return betrag;
    }
}
