package service;

import data.identifier.UserId;
import data.user.User;
import data.user.UserName;
import repository.UserRepository;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import service.serviceexception.UserServiceException;
import validator.Validator;
import java.sql.SQLException;

public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository userRepository) {
        this.repo = userRepository;
    }

    public User ermittleUserByUserName(UserName userName) throws ServiceException {
        Validator.isValidUserName(userName);
        try {
            User gefundenerUser = repo.findUserByName(userName);
            if (gefundenerUser == null) {
                throw new UserServiceException(UserServiceException.Message.USER_NICHT_GEFUNDEN);
            } else return gefundenerUser;
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.BENUTZER_NOT_FOUND.addInfo(" | angegebener Name: " + userName));
        }
    }

    public User ermittleUserByUserId(UserId userid) throws ServiceException {
        try {
            User gefundenerUser = repo.getUserByUserId(userid);
            if (gefundenerUser == null) {
                throw new UserServiceException(UserServiceException.Message.USER_NICHT_GEFUNDEN);
            } else return gefundenerUser;

        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }
}