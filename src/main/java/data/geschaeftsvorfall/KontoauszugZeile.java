package data.geschaeftsvorfall;

import data.user.UserName;
import menu.helper.CurrencyFormatter;

import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class KontoauszugZeile {

//Die Klasse ist nur zum Angucken, quasi das view Objekt von Kontauszugszeile


    private final Date datum;
    private final Optional<UserName> empfaenger;
    private final UserName sender;
    private final String beschreibung;
    private final GevoArt art;
    private final double betrag;

    public KontoauszugZeile(Date datum, Optional<UserName> empfaenger, UserName sender, String beschreibung, double betrag, GevoArt art) {
        this.datum = datum;
        this.empfaenger = empfaenger;
        this.sender = sender;
        this.beschreibung = beschreibung;
        this.art = art;
        this.betrag = betrag;
    }

    @Override
    public String toString() {
        return
                datum.toInstant()
                        .atZone(ZoneId.systemDefault()) // Zeitzone definieren
                        .toLocalDate() + " | "
                        + getEmpfaenger() + " | "
                        + sender.toString() + " | "
                        + beschreibung + " | "
                        + CurrencyFormatter.formatCurrency(betrag);
    }

    public Date getDatum() {
        return datum;
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
