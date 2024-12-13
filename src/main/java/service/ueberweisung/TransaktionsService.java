package service.ueberweisung;

import data.anweisungen.AbhebungsAnweisung;
import data.anweisungen.UeberweisungsAnweisung;
import repository.konto.KontoRepository;
import service.GevoService;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import validator.Validator;

import java.sql.SQLException;

public class TransaktionsService {

    private final KontoRepository kontoRepository;
    private final GevoService gevoService;

    public TransaktionsService(KontoRepository kontoRepository, GevoService gevoService) {
        this.kontoRepository = kontoRepository;
        this.gevoService = gevoService;
    }


    public void abheben(AbhebungsAnweisung anweisung) throws ServiceException {

        Validator.isValidBetrag(anweisung.getBetrag());

        double kontostand;

        try {
            kontostand = kontoRepository.ladeKontoStandVonKonto(anweisung.getKontoId());
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }

        TransaktionsValidator.validteAbhebungsAnweisung(
                anweisung,
                kontostand);

        try {
            kontoRepository.abheben(anweisung);
            gevoService.doc(anweisung);
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }


    }


    public void massenUeberweisung() throws ServiceException {


    }


    public void einzelUeberweisung(UeberweisungsAnweisung anweisung) throws ServiceException {

        double kontoStandSender;

        try {
            kontoStandSender = kontoRepository.ladeKontoStandVonKonto(anweisung.getSenderId());
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }

        /// todo das Erstellen von der Service-Exception wird hier vom UeberweisungValidator übernommen, ist irgendwie mega undurchsichtig, weil die methode legit nur zum exc thrown da ist
        TransaktionsValidator.validateUeberweisung(
                anweisung,
                kontoStandSender);

        try {
            kontoRepository.ueberweisen(anweisung);
            gevoService.doc(anweisung);
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }

    }


}
