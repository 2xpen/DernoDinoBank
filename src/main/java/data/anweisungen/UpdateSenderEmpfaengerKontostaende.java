package data.anweisungen;

import data.identifier.KontoId;

public class UpdateSenderEmpfaengerKontostaende {

    private final KontoId senderId;
    private final double neuerSenderKontoStand;

    private final KontoId empfaengerId;
    private final double neuerEmpfeangerKontoStand;

    public UpdateSenderEmpfaengerKontostaende(KontoId senderId, double neuerSenderKontoStand, KontoId empfaengerId, double neuerEmpfeangerKontoStand) {
        this.senderId = senderId;
        this.neuerSenderKontoStand = neuerSenderKontoStand;
        this.empfaengerId = empfaengerId;
        this.neuerEmpfeangerKontoStand = neuerEmpfeangerKontoStand;
    }

    public KontoId getEmpfaengerId() {
        return empfaengerId;
    }

    public KontoId getSenderId() {
        return senderId;
    }

    public double getNeuerEmpfeangerKontoStand() {
        return neuerEmpfeangerKontoStand;
    }

    public double getNeuerSenderKontoStand() {
        return neuerSenderKontoStand;
    }

}
