package data.pinnwand;


import data.identifier.PinnwandEntryId;
import data.identifier.UserId;

import java.sql.Timestamp;

public class PinnwandEntry {

    private final String nachricht;
    private final UserId autor_id;
    private final PinnwandEntryId eintragId;
    private final Timestamp date;

    public PinnwandEntry(String nachricht, UserId autor_id, Timestamp date) {
        this.autor_id = autor_id;
        this.eintragId = new PinnwandEntryId();
        this.nachricht = nachricht;
        this.date = date;
    }

    public String getNachricht() {
        return nachricht;
    }

    public UserId getAutor_id() {
        return autor_id;
    }
    public Timestamp getDate() {return date;}

    public PinnwandEntryId getEintragId() {
        return eintragId;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Text: ").append(nachricht).append("\n");
        return sb.toString();
    }
}
