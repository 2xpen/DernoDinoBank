package service;

import data.konto.Konto;
import data.user.Passwort;
import data.user.User;
import data.user.UserName;
import repository.UserRepository;
import repository.konto.KontoRepository;
import service.serviceexception.RegistrierungException;
import service.serviceexception.ServiceException;
import validator.Validator;

import java.sql.SQLException;

public class RegistrierungService {
    private final UserRepository userRepository;
    private final KontoRepository kontoRepository;

    public RegistrierungService(UserRepository userRepository, KontoRepository kontoRepository) {
        this.userRepository = userRepository;
        this.kontoRepository = kontoRepository;
    }

    public void registrieren(UserName userName, Passwort passwort) throws ServiceException {
        Validator.isValidUserName(userName);
        Validator.isValidPasswort(passwort);

        User createdUser = User.createUser(userName);

        try {
            if (userRepository.userNameExists(userName)) {
                throw new RegistrierungException(RegistrierungException.Message.BENUTZERNAME_BEREITS_VERGEBEN);
            }

            userRepository.createUser(createdUser, passwort);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            kontoRepository.createKonto(new Konto(
                    createdUser.getUserId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}