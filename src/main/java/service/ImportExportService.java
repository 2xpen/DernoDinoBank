package service;

import csv.CSVException;
import csv.CSV_Handler;
import csv.ICSV_IMPORT_EXPORT;
import data.KontoauszugWrapper;
import data.anweisungen.UeberweisungsAnweisungParam;
import data.geschaeftsvorfall.GevoZeile;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.identifier.UserId;
import data.nachricht.Nachricht;
import data.nachricht.NachrichtView;
import data.pinnwand.PinnwandEntry;
import data.pinnwand.PinnwandEntryView;
import data.user.UserName;
import service.serviceexception.ImportExportServiceException;
import service.serviceexception.ServiceException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImportExportService {

    private final UserService userService;
    private final KontoService kontoService;

    ICSV_IMPORT_EXPORT csvHandler = new CSV_Handler();

    public ImportExportService(UserService userService, KontoService kontoService) {
        this.userService = userService;
        this.kontoService = kontoService;
    }

    public List<UeberweisungsAnweisungParam> importMassenUeberweisung(Path path) throws ImportExportServiceException {
        try{
            return csvHandler.importMassenueberweisung(path);
        } catch (CSVException e) {
            throw new ImportExportServiceException(e.getServiceErrorMessage());
        }

    }

    public void exportDirectMessages(List<Nachricht> nachrichten, Path path) throws ServiceException {
        try {
            csvHandler.exportNachrichten(demaskNachrichten(nachrichten),path);
        } catch (CSVException e) {
            throw new ImportExportServiceException(e.getServiceErrorMessage());
        }

    }



    public void exportKontobewegungen(KontoauszugWrapper gevos, Path path) throws ServiceException {
        try {
            csvHandler.exportKontoAuszuege(gevos,path);
        } catch (CSVException e) {
            throw new ImportExportServiceException(e.getServiceErrorMessage());
        }
    }

    public void exportPinnwandnachrichten(List<PinnwandEntry> pinnwandEntries, Path path) throws ServiceException {
        try {
            csvHandler.exportPinnwandnachrichten(demaskPinnwandEntry(pinnwandEntries),path);
        } catch (CSVException e) {
            throw new ImportExportServiceException(e.getServiceErrorMessage());
        }
    }


    private List<PinnwandEntryView> demaskPinnwandEntry(List<PinnwandEntry> pinnwandEntrys) throws ServiceException {

        List<PinnwandEntryView> pinnwandEntryViewList = new ArrayList<>();
        for (PinnwandEntry pinnwandEntry : pinnwandEntrys) {

            pinnwandEntryViewList.add(
                    new PinnwandEntryView(
                    pinnwandEntry
                    ,userService.ermittleUserByUserId(pinnwandEntry.getBesitzer_id()).getUsername()
                    ,userService.ermittleUserByUserId(pinnwandEntry.getAutor_id()).getUsername()
            )

            );

        }
        return pinnwandEntryViewList;
    }


    public List<NachrichtView> demaskNachrichten(List<Nachricht> nachrichten) throws ServiceException {

        List<NachrichtView> nachrichtenView = new ArrayList<>();

        for(Nachricht nachricht: nachrichten){
            nachrichtenView.add(
                    new NachrichtView(
                            nachricht.getDate(),
                            userService.ermittleUserByUserId(nachricht.getSender()).getUsername(),
                            userService.ermittleUserByUserId(nachricht.getEmpfaenger()).getUsername(),
                            nachricht.getMessage()
                    )
            );
        }
        return nachrichtenView;
    }





    public List<KontoauszugZeile> demaskGevo(List<GevoZeile> gevos) throws ServiceException {

        List<KontoauszugZeile> kontoauszugZeilen = new ArrayList<>();

        for (GevoZeile gevo : gevos) {
            /// das hier ist highlight
            UserName empfaenger = gevo.getEmpfaenger().isPresent() ? userService.ermittleUserByUserId(kontoService.ermittelUserIdByKontoId(gevo.getEmpfaenger().get())).getUsername() : null;
            UserName sender = userService.ermittleUserByUserId(kontoService.ermittelUserIdByKontoId(gevo.getSender())).getUsername();

            kontoauszugZeilen.add(
            new KontoauszugZeile(
                            gevo.getDatum()
                            , Optional.ofNullable(empfaenger)
                            , sender
                            , gevo.getBeschreibung()
                            , gevo.getBetrag()
                            , gevo.getArt()

            )
            );
        }
return kontoauszugZeilen;
    }
}


