package service;

import data.identifier.UserId;
import data.pinnwand.Pinnwand;
import data.pinnwand.PinnwandEntry;
import data.pinnwand.PinnwandEntryView;
import data.user.UserName;
import repository.UserRepository;
import repository.pinnwand.PinnwandRepository;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import validator.Validator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PinnwandService {
    private final PinnwandRepository pinnwandRepository;
    private final UserService userService;

    public PinnwandService(PinnwandRepository pinnwandRepository, UserService userService) {
        this.pinnwandRepository = pinnwandRepository;
        this.userService = userService;
    }

    public Pinnwand getPinnwand(UserId userId) throws ServiceException {
        try {
            return convertPinnwandEntriesToPinnwand(pinnwandRepository.getPinnwandByUserId(userId));
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }

    public void schreibenAufAnderePinnwand(String message, UserId autor_id, UserId besitzer_id) throws ServiceException {
        Validator.isValidBeschreibung(message);
        try {
            PinnwandRepository.createPinnwandentry(message, autor_id, besitzer_id,new Timestamp(System.currentTimeMillis()));
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }

    public List<PinnwandEntry> filterafterAnforderung11(Pinnwand pinnwand, UserId besitzer, UserId selectedUser){
        List<PinnwandEntry> filteredEntries = new ArrayList<>();
            for (PinnwandEntry entry : pinnwand.getPinnwandentries()){
                if(entry.getBesitzer_id().equals(besitzer)  && entry.getAutor_id().equals(selectedUser)){
                    filteredEntries.add(entry);
                }
            }

        return filteredEntries;
    }

    public Pinnwand convertPinnwandEntriesToPinnwand(List<PinnwandEntry> pinnwandEntries) throws ServiceException {
        Pinnwand pinnwand = new Pinnwand();

        for (PinnwandEntry entry : pinnwandEntries) {
            UserName empfaengerName = userService.ermittleUserByUserId(entry.getBesitzer_id()).getUsername();
            UserName authorName = userService.ermittleUserByUserId(entry.getAutor_id()).getUsername();
            pinnwand.add(new PinnwandEntryView(entry,empfaengerName, authorName));
        }
        return pinnwand;
    }
}