package service.serviceexception;

public class AnmeldeServiceException extends ServiceException {
    public AnmeldeServiceException(ServiceErrorMessageProvider messageProvider) {
        super(ServiceErrorArt.VALIDATEERROR, messageProvider);
    }

    public enum Message implements ServiceErrorMessageProvider {
        BENUTZERNAME_NICHT_VERGEBEN("der Benutzername ist nicht vergeben"),
        PASSWORT_NICHT_KORREKT("Passwort ist nicht korrekt");

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