package service.ueberweisung;

import data.anweisungen.AbhebungsAnweisung;
import data.anweisungen.UeberweisungsAnweisung;
import data.anweisungen.UeberweisungsAnweisungParam;
import data.identifier.KontoId;
import repository.konto.KontoRepository;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import service.serviceexception.validateexception.ValidateBetragException;
import service.serviceexception.validateexception.ValidateUeberweisungException;
import validator.Validator;

import java.sql.SQLException;
import java.util.List;



public class
TransaktionsValidatorService {

//todo ueberweisung und abhebung zusammenfassen / durch interface der anweisung zb

    private final KontoRepository kontoRepository;

    public TransaktionsValidatorService(KontoRepository repository) {
        this.kontoRepository = repository;
    }


    /// also bisher wird nur validiert ob genug geld aufm konto liegt
    public void validateUeberweisung(UeberweisungsAnweisung anweisung) throws ServiceException {

        Validator.isValidBetrag(anweisung.getBetrag());
        Validator.isValidBeschreibung(anweisung.getBeschreibung());
        ueberziehtSaldo(anweisung.getSenderId(), anweisung.getBetrag());

    }




    public void isValidMassenueberweisungen(List<UeberweisungsAnweisungParam> paramList, KontoId kontoId) throws ServiceException {

        double ueberweisungsSumme = paramList.stream()
                .mapToDouble(
                        UeberweisungsAnweisungParam::getBetrag)
                .sum();

        ueberziehtSaldo(kontoId, ueberweisungsSumme);

    }





    public void validteAbhebungsAnweisung(AbhebungsAnweisung anweisung) throws ServiceException {
        Validator.isValidBetrag(anweisung.getBetrag());
        ueberziehtSaldo(anweisung.getKontoId(),anweisung.getBetrag());
    }

    private void ueberziehtSaldo(KontoId id, double betrag) throws ServiceException {

        try {

            if(!(kontoRepository.ladeKontoStandVonKonto(id) >= betrag)){
                 throw new ValidateUeberweisungException(ValidateUeberweisungException.Message.BETRAG_UEBERZIEHT_SALDO);
             }

        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.KONTOSTAND_LADEN_FEHLGESCHLAGEN);
        }

    }

}
