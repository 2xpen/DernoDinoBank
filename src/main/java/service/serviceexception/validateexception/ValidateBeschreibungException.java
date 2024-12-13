package service.serviceexception.validateexception;

import service.serviceexception.ServiceErrorMessageProvider;

public class ValidateBeschreibungException extends ValidateException {

    public ValidateBeschreibungException(ServiceErrorMessageProvider messageProvider) {
        super(messageProvider, Type.BESCHREIBUNG);
    }

    public enum Message implements ServiceErrorMessageProvider {

        BESCHREIBUNG("Mindestens ein zeichen, maximal 200 für eine Beschreibung");
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
