package service.serviceexception;

public class UserServiceException extends ServiceException {
    public UserServiceException(ServiceErrorMessageProvider message) {
        super(ServiceErrorArt.USERSERVICE, message);
    }

    public enum Message implements ServiceErrorMessageProvider {
        USER_NICHT_GEFUNDEN("der angegeben User konnte nicht gefunden werden!");

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