package service.serviceexception.validateexception;

import data.user.UserName;
import service.serviceexception.ServiceErrorMessageProvider;

public class ValidateUsernameException extends ValidateException {
    public ValidateUsernameException(Message messageProvider) {
        super(messageProvider, Type.USERNAME);
    }

    public enum Message implements ServiceErrorMessageProvider {
        USERNAME_INVALID("Username: %s ist nicht valide, mind 1 max 100 zeichen und folgendes Schema:"
                + "\n" + " tom.ramos@hsw-stud.de"
                + "\n" + " shinjiikari@nerv.jp"
                + "\n" + " dernovoj10xDev@dozent.org"
                + "\n" + " mrTapes@vodka.flasche.de");

        private String message;

        Message(String message) {
            this.message = message;
        }

        public Message addInfo(UserName name) {
            this.message = this.message.formatted(name.toString());
            return this;
        }

        @Override
        public String getServiceErrorMessage() {
            return message;
        }
    }
}