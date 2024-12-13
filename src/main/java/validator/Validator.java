package validator;

import data.user.Passwort;
import data.user.UserName;
import service.serviceexception.validateexception.*;

public class Validator {

    public static boolean isValidUserName(UserName name) throws ValidateUsernameException {
        if (REGEX.BENUTZERNAME.validate(name.toString())) {
            return true;
        } else {
            throw new ValidateUsernameException(ValidateUsernameException.Message.USERNAME_INVALID.addInfo(name));
        }
    }

    public static boolean isValidBetrag(double betrag) throws ValidateBetragException {
        String betragsString = String.valueOf(betrag);
        if (REGEX.BETRAG.validate(betragsString)) {
            return true;
        } else
            throw new ValidateBetragException(ValidateBetragException.Message.BETRAG_NICHT_KORREKT.addBetrag(betragsString));

    }

    public static boolean isValidBeschreibung(String beschreibung) throws ValidateBeschreibungException {
        if (REGEX.BESCHREIBUNG.validate(beschreibung)) {
            return true;
        } else throw new ValidateBeschreibungException(ValidateBeschreibungException.Message.BESCHREIBUNG);
    }

    public static boolean isValidPasswort(Passwort passwort) throws ValidatePasswortException {

        if (REGEX.BESCHREIBUNG.validate(passwort.toString())) {
            return true;
        } else throw new ValidatePasswortException(ValidatePasswortException.Message.PASSWORT);
    }


}
