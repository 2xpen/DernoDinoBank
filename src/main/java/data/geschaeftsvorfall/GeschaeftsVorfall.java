package data.geschaeftsvorfall;

import data.identifier.GevoId;
import data.identifier.KontoId;
import data.identifier.UserId;

import java.sql.Timestamp;

public abstract class GeschaeftsVorfall {

    private final GevoId gevoId;
    private final GevoArt art;
    private final KontoId senderId;
    private final Timestamp timestamp;

    protected GeschaeftsVorfall(GevoArt art, KontoId senderId) {
        this.gevoId = new GevoId();
        this.art = art;
        this.senderId = senderId;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public GevoArt getArt() {
        return art;
    }

    public GevoId getGevoId() {
        return gevoId;
    }

    public KontoId getSenderId() {
        return senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

}
