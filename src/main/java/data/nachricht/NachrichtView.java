package data.nachricht;

import data.identifier.UserId;
import data.user.UserName;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class NachrichtView {

    private final Timestamp date;
    private final UserName sender;
    private final UserName empfaenger;
    private final String message;

    public NachrichtView(Timestamp date, UserName senderName, UserName empfaengerName, String message) {
        this.date = date;
        this.sender = senderName;
        this.empfaenger = empfaengerName;
        this.message = message;
    }

    public String getEmpfaenger() {
        return empfaenger.toString();
    }

    public String getSender() {
        return sender.toString();
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.toLocalDateTime().format(formatter);
    }

}
