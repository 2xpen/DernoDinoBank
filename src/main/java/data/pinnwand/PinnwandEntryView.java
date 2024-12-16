package data.pinnwand;

import data.identifier.PinnwandEntryId;
import data.identifier.UserId;
import data.user.UserName;

public class PinnwandEntryView extends PinnwandEntry {
    private final UserName empfaengerName;
    private final UserName autorName;

    public PinnwandEntryView(PinnwandEntry entry, UserName empfaengerName,UserName autorName) {
        super(entry.getNachricht(), entry.getBesitzer_id(),entry.getAutor_id());
        this.empfaengerName = empfaengerName;
        this.autorName = autorName;
    }

    public UserName getAutorName() {
        return autorName;
    }
    public UserName getEmpfaengerName() {return empfaengerName;}

    public String toString(int counter) {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------\n") // Trennlinie oben
                .append("Entry Nummer: ").append(counter).append("\n") // Counter
                .append("Autor: ").append(autorName).append("\n") // Autorname
                .append("Text: ").append(getNachricht()).append("\n") // Nachricht
                .append("--------------------------"); // Trennlinie unten
        return sb.toString();
    }


}
