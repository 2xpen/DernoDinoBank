package data.builder;

import data.identifier.KontoId;
import data.user.UserName;

import java.time.LocalDate;

public class KontoAuszugZeileBuilder {

    private KontoId kontoId;
    private LocalDate transaktionsDatum;
    private UserName empfaenger;
    private UserName sender;
    private String message;
    private double betrag;

    public KontoId getKontoId() {
        return kontoId;
    }

    public KontoAuszugZeileBuilder setKontoId(KontoId kontoId) {
        this.kontoId = kontoId;
        return this;
    }

    public LocalDate getTransaktionsDatum() {
        return transaktionsDatum;
    }

    public KontoAuszugZeileBuilder setTransaktionsDatum(LocalDate transaktionsDatum) {
        this.transaktionsDatum = transaktionsDatum;
        return this;
    }

    public UserName getEmpfaenger() {
        return empfaenger;
    }

    public KontoAuszugZeileBuilder setEmpfaenger(UserName empfaenger) {
        this.empfaenger = empfaenger;
        return this;
    }

    public UserName getSender() {
        return sender;
    }

    public KontoAuszugZeileBuilder setSender(UserName sender) {
        this.sender = sender;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public KontoAuszugZeileBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public double getBetrag() {
        return betrag;
    }

    public KontoAuszugZeileBuilder setBetrag(double betrag) {
        this.betrag = betrag;
        return this;
    }
}
