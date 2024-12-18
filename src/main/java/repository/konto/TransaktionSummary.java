package repository.konto;

import data.user.UserName;

public class TransaktionSummary {
    private final UserName empfaenger;
    private final double betrag;
    private final String beschreibung;

    TransaktionSummary(UserName name, double betrag, String beschreibung) {
        this.beschreibung = beschreibung;
        this.betrag = betrag;
        this.empfaenger = name;
    }

    public void print() {
        String sb = "Transaktionszusammenfassung" + "\n" +
                "Empfänger: " + empfaenger.toString() + "\n" +
                "Betrag: " + betrag + "\n" +
                "Beschreibung: " + beschreibung + "\n";
        System.out.println(sb);
    }

    public UserName getEmpfaenger() {
        return empfaenger;
    }

    public double getBetrag() {
        return betrag;
    }

    public String getBeschreibung() {
        return beschreibung;
    }
}