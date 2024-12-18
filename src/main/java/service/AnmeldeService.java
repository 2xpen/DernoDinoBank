package service;

import data.user.Passwort;
import data.user.User;
import data.user.UserName;
import repository.UserRepository;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import service.serviceexception.AnmeldeServiceException;
import validator.Validator;
import java.sql.SQLException;

public class AnmeldeService {
    private final UserRepository userRepository;

    public AnmeldeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User anmelden(UserName userName, Passwort passwort) throws ServiceException {
        Validator.isValidUserName(userName);
        Validator.isValidPasswort(passwort);

        try {
            User user = userRepository.anmelden(userName, passwort);
            if (user == null) {
                if (userRepository.userNameExists(userName)) {
                    throw new AnmeldeServiceException(AnmeldeServiceException.Message.PASSWORT_NICHT_KORREKT);
                } else {
                    throw new AnmeldeServiceException(AnmeldeServiceException.Message.BENUTZERNAME_NICHT_VERGEBEN);
                }
            } else {
                return user;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }
}