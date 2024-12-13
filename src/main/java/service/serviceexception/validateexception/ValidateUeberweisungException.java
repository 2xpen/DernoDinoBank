package service.serviceexception.validateexception;

import service.serviceexception.ServiceErrorMessageProvider;

public class ValidateUeberweisungException extends ValidateException {


    public ValidateUeberweisungException(Message message) {
        super(message, Type.UEBERWEISUNG);
    }

    public enum Message implements ServiceErrorMessageProvider {
        BETRAG_UEBERZIEHT_SALDO("du hast nicht soviel kohle du trottel");

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
