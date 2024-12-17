package service.serviceexception.validateexception;

import service.serviceexception.ServiceErrorMessageProvider;

public class ValidateUeberweisungException extends ValidateException {


    public ValidateUeberweisungException(Message message) {
        super(message, Type.UEBERWEISUNG);
    }

    public enum Message implements ServiceErrorMessageProvider {
        BETRAG_UEBERZIEHT_SALDO("Der Betrag überzieht ihr saldo"),
        MASSENUEBERWEISUNG_LEER("Massenanweisung nach import leer"),
        KEIN_KONTO_FUER_DEN_NAMEN("Es wurde kein Konto für folgenden Namen gefunden:");

        private String message;

        Message(String message) {
            this.message = message;
        }

        public Message addNamen(String namen){
            this.message += " " + namen;
            return this;
        }


        @Override
        public String getServiceErrorMessage() {
            return message;
        }
    }


}
