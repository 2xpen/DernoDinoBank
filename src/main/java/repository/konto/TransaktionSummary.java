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
        StringBuilder sb = new StringBuilder();

        sb.append("Transaktionszusammenfassung").append("\n");
        sb.append("Empfänger: " + empfaenger.toString()).append("\n");
        sb.append("Betrag: " + betrag).append("\n");
        sb.append("Beschreibung: " + beschreibung).append("\n");

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