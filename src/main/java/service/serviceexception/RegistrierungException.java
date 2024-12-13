package service.serviceexception;

public class RegistrierungException extends ServiceException {

    public RegistrierungException(ServiceErrorMessageProvider message) {
        super(ServiceErrorArt.REGISTRATIONERROR, message);
    }

    public enum Message implements ServiceErrorMessageProvider {
        BENUTZERNAME_BEREITS_VERGEBEN("der gewählte Benutzername exsistiert bereits");

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
