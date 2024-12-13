package data.anweisungen;

import data.user.UserName;

public class UeberweisungsAnweisungParam {
    private final String beschreibung;
    private final UserName empfeangerName;
    private final double betrag;

    public UeberweisungsAnweisungParam(String empfeangerName, String beschreibung, double betrag) {
        this.beschreibung = beschreibung;
        this.empfeangerName = new UserName(empfeangerName);
        this.betrag = betrag;
    }

    public double getBetrag() {
        return betrag;
    }

    public String getEmpfeanger() {
        return empfeangerName.toString();
    }

    public String getBeschreibung() {
        return beschreibung;
    }

}