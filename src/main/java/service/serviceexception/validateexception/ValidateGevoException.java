package service.serviceexception.validateexception;

import service.serviceexception.ServiceErrorMessageProvider;

public class ValidateGevoException extends ValidateException {

    public ValidateGevoException(ServiceErrorMessageProvider messageProvider) {
        super(messageProvider, Type.GEVO);
    }

    public enum Message implements ServiceErrorMessageProvider {
        GEVOERSTELLEN_FEHLSCHLAG("der Vorgang konnte leider nicht dokumentiert werden," +
                " sie sollten diese Meldung niemals sehen, melden sie diesen Fehler bitte an das Entwickler-Team" +
                "wir bitten vielmals um entschuldigung");

        private final String message;

        Message(String message) {
            this.message = message;
        }

        @Override
        public String getServiceErrorMessage() {
            return message;
        }
    }
}