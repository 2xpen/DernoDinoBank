package service.serviceexception.validateexception;

import service.serviceexception.ServiceErrorMessageProvider;

public class ValidatePasswortException extends ValidateException {
    public ValidatePasswortException(ServiceErrorMessageProvider messageProvider) {
        super(messageProvider, Type.PASSWORT);
    }

    public enum Message implements ServiceErrorMessageProvider {
        PASSWORT("Ein passwort brauch mindestens 1 Zeichen und darf maximal 30 Zeichen haben");
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