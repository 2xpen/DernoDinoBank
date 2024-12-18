package service;

import data.identifier.UserId;
import data.nachricht.Nachricht;
import data.nachricht.NachrichtView;
import data.user.User;
import repository.directmessages.DirectMessagesRepository;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import validator.Validator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private final DirectMessagesRepository directMessagesRepository;
    private final UserService userService;

    public MessageService(DirectMessagesRepository directMessagesRepository, UserService userService) {
        this.directMessagesRepository = directMessagesRepository;
        this.userService = userService;
    }

    public List<Nachricht> getConvo(User selector, User selectedUser) throws ServiceException {
        try {
            return directMessagesRepository.getConvo(selector.getUserId(),selectedUser.getUserId());
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
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