package service.ueberweisung;
import data.anweisungen.*;
import data.identifier.KontoId;
import data.user.UserName;
import repository.konto.KontoRepository;
import service.GevoService;
import service.ImportExportService;
import service.KontoService;
import service.UserService;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateUeberweisungException;
import validator.Validator;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransaktionsService {

    private final KontoRepository kontoRepository;
    private final GevoService gevoService;
    private final TransaktionsValidatorService transaktionsValidatorService;
    private final KontoService kontoService;
    private final UserService userService;

    private final ImportExportService importExportService;
    public TransaktionsService(KontoRepository kontoRepository, GevoService gevoService, ImportExportService importExportService, TransaktionsValidatorService transaktionsValidatorService, KontoService kontoService, UserService userService) {
        this.kontoRepository = kontoRepository;
        this.gevoService = gevoService;
        this.transaktionsValidatorService = transaktionsValidatorService;
        this.importExportService = importExportService;
        this.kontoService = kontoService;
        this.userService = userService;
    }


    public void abheben(AbhebungsAnweisung anweisung) throws ServiceException {

        Validator.isValidBetrag(anweisung.getBetrag());

        transaktionsValidatorService.validteAbhebungsAnweisung(anweisung);

       UpdateAbhebenKontostand updateAbhebenKontostand = calculateNewBalanceWrapper(anweisung);

        try {
            kontoRepository.abheben(updateAbhebenKontostand);
            gevoService.doc(anweisung);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }

    }


    public void massenUeberweisung(Path quellpfad, KontoId senderId) throws ServiceException {

        List<UeberweisungsAnweisungParam> ueberweisungsAnweisungParams = importExportService.importMassenUeberweisung(quellpfad);

        transaktionsValidatorService.isValidMassenueberweisungen(ueberweisungsAnweisungParams,senderId);

        List<UeberweisungsAnweisung> ueberweisungsAnweisungen = convertMassenUeberweisung(ueberweisungsAnweisungParams,senderId);

        if(ueberweisungsAnweisungen.isEmpty()) {
            throw new ValidateUeberweisungException(ValidateUeberweisungException.Message.MASSENUEBERWEISUNG_LEER);
        }

        for (UeberweisungsAnweisung anweisung :ueberweisungsAnweisungen ){
            einzelUeberweisung(anweisung);

        }

    }

    private List<UeberweisungsAnweisung> convertMassenUeberweisung(List<UeberweisungsAnweisungParam> paramList, KontoId senderId) throws ServiceException {

        List<UeberweisungsAnweisung> anweisungen = new ArrayList<>();

        int i = 0;
        for (UeberweisungsAnweisungParam param : paramList) {
                i++;
            anweisungen.add(new UeberweisungsAnweisung(
                    senderId
                    ,getKontoIdByUserName(new UserName(param.getEmpfeanger()),i)
                    ,param.getBeschreibung()
                    ,param.getBetrag()

            )

            );
        }
        return anweisungen;
    }


    public void einzelUeberweisung(UeberweisungsAnweisung anweisung) throws ServiceException {

        /// todo das Erstellen von der Service-Exception wird hier vom UeberweisungValidator übernommen, ist irgendwie mega undurchsichtig, weil die methode legit nur zum exc thrown da ist
        transaktionsValidatorService.validateUeberweisung(anweisung);

           UpdateSenderEmpfaengerKontostaende stmt = calculateNewBalancesWrapper(anweisung);
        try {
            kontoRepository.ueberweisen(stmt);
            gevoService.doc(anweisung);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }

    }


    private UpdateSenderEmpfaengerKontostaende calculateNewBalancesWrapper(UeberweisungsAnweisung anweisung) throws ServiceException {

        try {

            double neuerKontoStandSender = kontoRepository.ladeKontoStandVonKonto(anweisung.getSenderId()) - anweisung.getBetrag();
            double neuerKontoStandEmpfeanger = kontoRepository.ladeKontoStandVonKonto(anweisung.getEmpfaengerId()) + anweisung.getBetrag();

            return new UpdateSenderEmpfaengerKontostaende(anweisung.getSenderId(),neuerKontoStandSender,anweisung.getEmpfaengerId(),neuerKontoStandEmpfeanger);
        }
        catch (SQLException e) {

            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }

    }



    private UpdateAbhebenKontostand calculateNewBalanceWrapper(AbhebungsAnweisung anweisung) throws ServiceException {

        try {
            double neuerKontostand = kontoRepository.ladeKontoStandVonKonto(anweisung.getKontoId())-anweisung.getBetrag();
            return new UpdateAbhebenKontostand(anweisung.getKontoId(),neuerKontostand);
        } catch (SQLException e) {

            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }

    }


    public KontoId getKontoIdByUserName(UserName userName,int i) throws ServiceException {

        KontoId kontoId =  kontoService.ermittelKontoIdByUserId(userService.ermittleUserByUserName(userName).getUserId());
        //gucken ob ein konto für ein namen exsitiert
        if (!kontoService.kontoExsist(kontoId)) {
            throw new ValidateUeberweisungException(ValidateUeberweisungException.Message.KEIN_KONTO_FUER_DEN_NAMEN.addNamen(userName.toString()+ ", Fehler war in der:"+i+"ten Stelle"));
        }
        return kontoId;

    }


}
