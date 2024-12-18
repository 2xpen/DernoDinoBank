package data.pinnwand;

import data.user.UserName;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PinnwandEntryView extends PinnwandEntry {
    private final UserName empfaengerName;
    private final UserName autorName;

    public PinnwandEntryView(PinnwandEntry entry, UserName empfaengerName,UserName autorName) {
        super(entry.getNachricht(), entry.getBesitzer_id(),entry.getAutor_id(),entry.getTimestamp());
        this.empfaengerName = empfaengerName;
        this.autorName = autorName;
    }

    public UserName getAutorName() {
        return autorName;
    }
    public UserName getEmpfaengerName() {return empfaengerName;}

    public String toString(int counter) {
        String sb = "--------------------------\n" + // Trennlinie oben
                "Eintragsnummer: " + counter + "\n" + // Counter
                "Autor: " + autorName + "\n" + // Autorname
                "Text: " + getNachricht() + "\n" + // Nachricht
                "--------------------------"; // Trennlinie unten
        return sb;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return getTimestamp().toLocalDateTime().format(formatter);
    }


}