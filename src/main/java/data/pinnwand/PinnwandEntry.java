package data.pinnwand;

import data.identifier.PinnwandEntryId;
import data.identifier.UserId;

import java.sql.Timestamp;

public class PinnwandEntry {

    private final String nachricht;
    private final UserId autor_id;
    private final UserId besitzer_id;
    private final PinnwandEntryId eintragId;
    private final Timestamp timestamp;

    public PinnwandEntry(String nachricht, UserId besitzer_id , UserId autor_id, Timestamp timestamp) {
        this.autor_id = autor_id;
        this.besitzer_id = besitzer_id;
        this.timestamp = timestamp;
        this.eintragId = new PinnwandEntryId();
        this.nachricht = nachricht;
    }

    public String getNachricht() {
        return nachricht;
    }

    public UserId getBesitzer_id() {
        return besitzer_id;
    }

    public UserId getAutor_id() {
        return autor_id;
    }

    public PinnwandEntryId getEintragId() {
        return eintragId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}