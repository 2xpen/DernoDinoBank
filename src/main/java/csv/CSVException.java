package csv;

public class CSVException extends Exception {

    public CSVException(Message message) {
        super(message.getMessage());
    }

    public enum Message {
        FileIstEmpty("Die Datei ist leer"),
        FileNotFound("Datei konnte nicht gefunden werden"),
        CSFFormat("Das format der Datei ist falsch"),
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
            this.message += be;
            return this;
        }


        public String getMessage() {
            return message;
        }
    }
}