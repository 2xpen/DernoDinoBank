package service.ueberweisung;

import data.anweisungen.AbhebungsAnweisung;
import data.anweisungen.UeberweisungsAnweisung;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import service.serviceexception.validateexception.ValidateBetragException;
import service.serviceexception.validateexception.ValidateUeberweisungException;
import validator.Validator;


public class
TransaktionsValidator {

//todo ueberweisung und abhebung zusammenfassen / durch interface der anweisung zb


    /// also bisher wird nur validiert ob genug geld aufm konto liegt
    public static void validateUeberweisung(UeberweisungsAnweisung anweisung, double kontostandSender) throws ValidateUeberweisungException, ValidateBetragException, ValidateBeschreibungException {

        Validator.isValidBetrag(anweisung.getBetrag());
        Validator.isValidBeschreibung(anweisung.getBeschreibung());
        if (anweisung.getBetrag() > kontostandSender) {
            throw new ValidateUeberweisungException(
                    ValidateUeberweisungException.Message.BETRAG_UEBERZIEHT_SALDO);
        }

    }


    public static void validteAbhebungsAnweisung(AbhebungsAnweisung anweisung, double kontostand) throws ValidateUeberweisungException, ValidateBetragException {


        Validator.isValidBetrag(anweisung.getBetrag());

        if (anweisung.getBetrag() > kontostand) {
            throw new ValidateUeberweisungException(
                    ValidateUeberweisungException.Message.BETRAG_UEBERZIEHT_SALDO);
        }

    }


}

