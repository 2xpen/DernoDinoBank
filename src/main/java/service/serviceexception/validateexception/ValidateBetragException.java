package service.serviceexception.validateexception;

import service.serviceexception.ServiceErrorMessageProvider;

public class ValidateBetragException extends ValidateException {

    public ValidateBetragException(ServiceErrorMessageProvider messageProvider) {
        super(messageProvider, Type.BETRAG);
    }

    public enum Message implements ServiceErrorMessageProvider {
        BETRAG_NICHT_KORREKT("""
                Folgender Betrag fehlerhaft: %s\
                
                -maximal 2 Nachkommastellen\
                
                - nicht mehr als 18 Stellen nach dem Komma\
                
                - größer als 0""");

        private String message;

        Message(String message) {
            this.message = message;
        }

        public Message addBetrag(String betrag) {
            this.message = this.message.formatted(betrag);
            return this;
        }

        @Override
        public String getServiceErrorMessage() {
            return message;
        }
    }
}