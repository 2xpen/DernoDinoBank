package repository.konto;

public enum TransaktionExitCode {
    TRANSAKTIONS_ERFOLGREICH("Transaktion erfolgreich"),
    EMPFAENGER_NICHT_GEFUNDEN("Der Empfänger wurde nicht gefunden"),
    VALIDIERUNG_FEHLGESCHLAGEN("Die Anweisung für die Überweisung ist nicht korrekt"),
    VERBINDUNG_ABGEBROCHEN("Verbindung abgebrochen");

    private final String message;
    private final Runnable action;

    TransaktionExitCode(String message) {
        this.message = message;
        this.action = () -> System.out.println(message);
    }

    public String getMessage() {
        return message;
    }
}