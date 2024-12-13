package service;

import data.identifier.UserId;
import data.nachricht.Nachricht;
import repository.directmessages.DirectMessagesRepository;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import validator.Validator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class MessageService {
    private final DirectMessagesRepository directMessagesRepository;

    public MessageService(DirectMessagesRepository directMessagesRepository) {
        this.directMessagesRepository = directMessagesRepository;
    }

    public List<Nachricht> getNachrichten(UserId userId) throws ServiceException {
        try {
            return directMessagesRepository.getNachrichtenByUserId(userId);
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean sendMessage(Timestamp date, UserId sender, UserId empfaenger, String message) throws DatenbankException, ValidateBeschreibungException {

        Validator.isValidBeschreibung(message);

        try {
            directMessagesRepository.createDirectMessage(date, sender, empfaenger, message);
            return true;
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }
}
