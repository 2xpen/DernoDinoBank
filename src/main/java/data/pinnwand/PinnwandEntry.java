package data.pinnwand;


import data.identifier.PinnwandEntryId;
import data.identifier.UserId;

public class PinnwandEntry {

    private final String nachricht;
    private final UserId autor_id;
    private final PinnwandEntryId eintragId;

    public PinnwandEntry(String nachricht, UserId autor_id) {
        this.autor_id = autor_id;
        this.eintragId = new PinnwandEntryId();
        this.nachricht = nachricht;
    }

    public String getNachricht() {
        return nachricht;
    }

    public UserId getAutor_id() {
        return autor_id;
    }

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
