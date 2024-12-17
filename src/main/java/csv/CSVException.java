package csv;
import service.serviceexception.ServiceErrorMessageProvider;

public class CSVException extends Exception {

    private final Message message;

    public CSVException(Message message) {
        super(message.getServiceErrorMessage());
        this.message = message;
    }

    // todo hier bitte mal diskutieren wie man die strukur hier besser halten könnte
    public Message getServiceErrorMessage() {
        return message;
    }

    public enum Message implements ServiceErrorMessageProvider {
        FileIstEmpty("Die Datei ist leer"),
        NichtsZumExportieren("es wurde nichts zum exportieren gefunden"),
        FileNotFound("Datei konnte nicht gefunden werden"),
        CSVFormat("Syntax fehler in der Datei"),
        ExportIsEmpty("Die zu exportierende Datei ist leer"),
        WriteFailed("Das schreiben der Datei ist Fehlgeschlagen"),
        NumberFormat("Der Betrag ist keine gültige Zahl");

        private String message = "";

        Message(String message) {
            this.message = message;
        }

        public Message addZeile(int zeile) {
            this.message += " in Zeile: " + zeile;
            return this;
        }

        public Message addPath(String path) {
            this.message += " im Pfad: " + path;
            return this;
        }

        public Message addInfo(String be) {
            this.message += " "+be;
            return this;
        }


        public String getServiceErrorMessage() {
            return message;
        }
    }


}