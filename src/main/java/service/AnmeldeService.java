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

        /// hier wird der Username validiert, würde auch eine Exception thrown ohne dass das grad ersichtlich ist
        Validator.isValidUserName(userName);
        Validator.isValidPasswort(passwort);

        try {
            User user = userRepository.anmelden(userName, passwort);

            //Fehlerbehandlung falls kein User zurück kam
            if (user == null) {
                //ist der name überhaupt vergeben? Falls ja ist das passwort falsch, falls nein dann war der benutzername gar nicht vergeben
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