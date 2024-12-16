package service.ueberweisung;

import data.anweisungen.AbhebungsAnweisung;
import data.anweisungen.UeberweisungsAnweisung;
import data.anweisungen.UeberweisungsAnweisungParam;
import data.identifier.KontoId;
import data.identifier.UserId;
import repository.konto.KontoRepository;
import service.GevoService;
import service.ImportExportService;
import service.KontoService;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import validator.Validator;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransaktionsService {

    private final KontoRepository kontoRepository;
    private final GevoService gevoService;
    private final TransaktionsValidatorService transaktionsValidatorService;

    private final ImportExportService importExportService;
    public TransaktionsService(KontoRepository kontoRepository, GevoService gevoService, ImportExportService importExportService, TransaktionsValidatorService transaktionsValidatorService) {
        this.kontoRepository = kontoRepository;
        this.gevoService = gevoService;
        this.transaktionsValidatorService = transaktionsValidatorService;
        this.importExportService = importExportService;
    }


    public void abheben(AbhebungsAnweisung anweisung) throws ServiceException {

        Validator.isValidBetrag(anweisung.getBetrag());

        transaktionsValidatorService.validteAbhebungsAnweisung(anweisung);

        try {
            kontoRepository.abheben(anweisung);
            gevoService.doc(anweisung);
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }


    }


    public void massenUeberweisung(String quellpfad, KontoId senderId) throws ServiceException {

        List<UeberweisungsAnweisungParam> ueberweisungsAnweisungParams = importExportService.importMassenUeberweisung(Path.of(quellpfad));

        transaktionsValidatorService.isValidMassenueberweisungen(ueberweisungsAnweisungParams,senderId);

        try {
            for (UeberweisungsAnweisungParam anweisung : ueberweisungsAnweisungParams) {


                kontoRepository.ueberweisen(null);


            }

        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }



    }



    private List<UeberweisungsAnweisung> converMassenUeberweisung(List<UeberweisungsAnweisungParam> paramList, KontoId senderId) throws ServiceException {

        List<UeberweisungsAnweisung> anweisungen = new ArrayList<>();

        for (UeberweisungsAnweisungParam param : paramList) {
  /*          anweisungen.add(
                    new UeberweisungsAnweisung(



                    )
            )*/
        }



return null;

    }





    public void einzelUeberweisung(UeberweisungsAnweisung anweisung) throws ServiceException {


        /// todo das Erstellen von der Service-Exception wird hier vom UeberweisungValidator übernommen, ist irgendwie mega undurchsichtig, weil die methode legit nur zum exc thrown da ist
        transaktionsValidatorService.validateUeberweisung(anweisung);

        try {
            kontoRepository.ueberweisen(anweisung);
            gevoService.doc(anweisung);
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }

    }


}
