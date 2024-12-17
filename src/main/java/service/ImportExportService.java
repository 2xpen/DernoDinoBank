package service;

import csv.CSVException;
import csv.CSV_Handler;
import csv.ICSV_IMPORT_EXPORT;
import data.anweisungen.UeberweisungsAnweisungParam;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.identifier.UserId;
import data.nachricht.Nachricht;
import data.nachricht.NachrichtView;
import data.pinnwand.PinnwandEntry;
import data.pinnwand.PinnwandEntryView;
import service.serviceexception.ImportExportServiceException;
import service.serviceexception.ServiceException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ImportExportService {

    private final UserService userService;

    ICSV_IMPORT_EXPORT csvHandler = new CSV_Handler();

    public ImportExportService(UserService userService) {
        this.userService = userService;
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

}
