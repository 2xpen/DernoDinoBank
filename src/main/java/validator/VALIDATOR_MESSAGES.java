package validator;

public enum VALIDATOR_MESSAGES {

BENUTZERNAME_INVALID("Benutzername ist nicht valide");



private final String message;
private final Runnable action;

VALIDATOR_MESSAGES(String message) {
    this.message = message;
    this.action = () -> System.out.println(message);
}

    public void print(){
    this.action.run();
    }

}
