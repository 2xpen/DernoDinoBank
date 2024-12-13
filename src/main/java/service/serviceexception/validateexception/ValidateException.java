package service.serviceexception.validateexception;

import service.serviceexception.ServiceErrorArt;
import service.serviceexception.ServiceErrorMessageProvider;
import service.serviceexception.ServiceException;

public abstract class ValidateException extends ServiceException {

    private final Type type;

    public ValidateException(ServiceErrorMessageProvider messageProvider, Type type) {
        super(ServiceErrorArt.VALIDATEERROR, messageProvider);
        this.type = type;
    }

    enum Type {
        UEBERWEISUNG,
        USERNAME,
        BETRAG,
        GEVO,
        BESCHREIBUNG,
        PASSWORT,
        ANMELDUNG;
    }


}
