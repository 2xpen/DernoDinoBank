package service.serviceexception;

public class DatenbankException extends ServiceException {

    public DatenbankException(Message dbErrorMessages) {
        super(ServiceErrorArt.DATENBANKERROR, dbErrorMessages);
    }


    public enum Message implements ServiceErrorMessageProvider {
        KONTOSTAND_VON_SENDER_LADEN("Das laden vom Kontostand des Sender ist fehlgeschlagen"),
        UEBERWEISUNG("Fehler beim senden der Überweisung an die Datenbank"),
        KONTOSTAND_LADEN_FEHLGESCHLAGEN("Kontostand konnte nicht geladen werden"),
        ANMELDUNG_FEHLGESCHLAGEN("Feler beim Anmeldeprozesss"),
        BENUTZER_NOT_FOUND("Für den angegebenen Namen konnte kein User gefunden werden"),
        INTERNAL_SERVER_ERROR("Problem mit dem Server");

        private String fehlerMessage;

        Message(String fehlerMessage) {
            this.fehlerMessage = fehlerMessage;
        }

        @Override
        public String getServiceErrorMessage() {
            return fehlerMessage;
        }

        public Message addInfo(String info) {
            fehlerMessage += info;
            return this;
        }

    }


}
