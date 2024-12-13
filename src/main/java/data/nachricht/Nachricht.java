package data.nachricht;

import data.identifier.UserId;

import java.sql.Timestamp;

public class Nachricht {

    private final Timestamp date;
    private final UserId sender;
    private final UserId empfaenger;
    private final String message;


    public Nachricht(Timestamp date, UserId sender, UserId empfaenger, String message) {
        this.date = date;
        this.sender = sender;
        this.empfaenger = empfaenger;
        this.message = message;
    }

    public UserId getEmpfaenger() {
        return empfaenger;
    }

    public UserId getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getDate() {
        return date;
    }
}
